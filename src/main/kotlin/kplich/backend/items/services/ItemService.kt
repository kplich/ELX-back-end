package kplich.backend.items.services

import kplich.backend.authentication.UserWithIdNotFoundException
import kplich.backend.items.entities.Item
import kplich.backend.items.entities.ItemPhoto
import kplich.backend.items.*
import kplich.backend.items.payloads.requests.ItemAddRequest
import kplich.backend.items.payloads.requests.ItemFilteringCriteria
import kplich.backend.items.payloads.requests.ItemUpdateRequest
import kplich.backend.items.payloads.responses.CategoryResponse
import kplich.backend.items.payloads.responses.ItemResponse
import kplich.backend.authentication.repositories.ApplicationUserRepository
import kplich.backend.core.findByIdOrThrow
import kplich.backend.items.repositories.CategoryRepository
import kplich.backend.items.repositories.ItemRepository
import kplich.backend.items.repositories.PhotoRepository
import kplich.backend.authentication.services.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemService(
        private val itemRepository: ItemRepository,
        private val userRepository: ApplicationUserRepository,
        private val categoryRepository: CategoryRepository,
        private val photoRepository: PhotoRepository
) {

    fun getCategories(): List<CategoryResponse> {
        return categoryRepository.findAll().map { it.toResponse() }
    }

    @Throws(BadEditItemRequestException::class, UnauthorizedItemAddingRequestException::class)
    @Transactional
    fun addItem(request: ItemAddRequest): ItemResponse {
        return itemRepository.save(request.mapToItem()).toResponse()
    }

    @Throws(ItemNotFoundException::class)
    @Transactional(readOnly = true)
    fun getItem(id: Long): ItemResponse {
        return itemRepository.findByIdOrThrow(id, ::ItemNotFoundException).toResponse()
    }

    @Throws(
            ItemNotFoundException::class,
            UnauthorizedItemUpdateRequestException::class,
            BadEditItemRequestException::class)
    @Transactional
    fun updateItem(request: ItemUpdateRequest): ItemResponse {
        val oldItem = itemRepository.findByIdOrThrow(request.id, ::ItemNotFoundException)
        if (oldItem.cannotBeUpdatedByCurrentlyLoggedUser())
            throw UnauthorizedItemUpdateRequestException(oldItem.id)
        if (oldItem.closed)
            throw ClosedItemUpdateException()

        return itemRepository.save(request.mapToItem(oldItem)).toResponse()
    }

    @Throws(
            ItemNotFoundException::class,
            UnauthorizedItemUpdateRequestException::class,
            ItemAlreadyClosedException::class
    )
    @Transactional
    fun closeItem(id: Long): ItemResponse {
        val item = itemRepository.findByIdOrThrow(id, ::ItemNotFoundException)

        if (item.cannotBeUpdatedByCurrentlyLoggedUser()) throw UnauthorizedItemUpdateRequestException(id)
        if (item.closed) throw ItemAlreadyClosedException(id)

        return itemRepository.save(item.close()).toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllOpenItems(filteringCriteria: ItemFilteringCriteria?): List<ItemResponse> {
        // TODO: implement relevance of of search results
        // TODO: some more sophisticated filtering method should be used
        return itemRepository.findAll().filter { item ->
            ItemFilteringCriteria.isNotClosed.test(item).and(
                    filteringCriteria?.testItemForFilters(item) ?: true)
        }.map {
            it.toResponse()
        }
    }

    @Throws(
            UnauthorizedItemAddingRequestException::class,
            UserWithIdNotFoundException::class,
            CategoryNotFoundException::class
    )
    private fun ItemAddRequest.mapToItem(): Item {
        // fields or many to one associations
        val title = this.title
        val description = this.description
        val price = this.price

        val loggedInId = UserService.getCurrentlyLoggedId()
                ?: throw UnauthorizedItemAddingRequestException()

        val addedBy = userRepository.findByIdOrThrow(loggedInId, ::UserWithIdNotFoundException)
        val category = categoryRepository.findByIdOrThrow(this.category, ::CategoryNotFoundException)
        val usedStatus = this.usedStatus

        // one to many association
        val itemSavedPrePhotos = itemRepository.save(Item(title, description, price, addedBy, category, usedStatus, mutableListOf()))

        val itemPhotos = this.photos.map {
            photoRepository.save(ItemPhoto(it, itemSavedPrePhotos))
        }.toMutableList()

        return itemSavedPrePhotos.apply {
            this.photos = itemPhotos
        }
    }

    private fun ItemUpdateRequest.mapToItem(oldItem: Item): Item {
        photoRepository.deleteAll(oldItem.photos)

        val category = categoryRepository.findByIdOrThrow(this.category, ::CategoryNotFoundException)

        val itemSavedPrePhotos = itemRepository.save(oldItem.apply {
            this.title = this@mapToItem.title
            this.description = this@mapToItem.description
            this.price = this@mapToItem.price
            this.category = category
            this.usedStatus = this@mapToItem.usedStatus
        })

        val itemPhotos = this.photos.map {
            photoRepository.save(ItemPhoto(it, itemSavedPrePhotos))
        }.toMutableList()

        return itemSavedPrePhotos.apply {
            this.photos = itemPhotos
        }
    }

    private fun Item.cannotBeUpdatedByCurrentlyLoggedUser(): Boolean {
        val loggedInId = UserService.getCurrentlyLoggedId()
        return loggedInId == null || this.addedBy.id != loggedInId
    }
}
