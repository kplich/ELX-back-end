package kplich.backend.services

import kplich.backend.entities.Item
import kplich.backend.entities.ItemPhoto
import kplich.backend.exceptions.*
import kplich.backend.payloads.requests.items.ItemAddRequest
import kplich.backend.payloads.requests.items.ItemUpdateRequest
import kplich.backend.payloads.responses.items.ItemResponse
import kplich.backend.repositories.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

// TODO: improve model mapping

@Service
class ItemService(
        private val itemRepository: ItemRepository,
        private val userRepository: ApplicationUserRepository,
        private val categoryRepository: CategoryRepository,
        private val photoRepository: PhotoRepository
) {

    @Throws(BadAddItemRequestException::class)
    @Transactional
    fun addItem(request: ItemAddRequest): ItemResponse {
        return itemRepository.save(request.mapToItem()).toResponse()
    }

    @Throws(ItemNotFoundException::class)
    fun getItem(id: Long): ItemResponse {
        return itemRepository.findByIdOrThrow(id, ::ItemNotFoundException).toResponse()
    }

    @Throws(
            ItemNotFoundException::class,
            UnauthorizedItemUpdateRequestException::class,
            NewItemCategoryNotFoundException::class)
    @Transactional
    fun updateItem(request: ItemUpdateRequest): ItemResponse {
        val oldItem = itemRepository.findByIdOrThrow(request.id, ::ItemNotFoundException)
        if (oldItem.cannotBeUpdatedByCurrentlyLoggedUser()) throw UnauthorizedItemUpdateRequestException(oldItem.id)
        if (oldItem.closed) throw ClosedItemUpdateException()

        return itemRepository.save(request.mapToItem(oldItem)).toResponse()
    }

    @Throws(ItemNotFoundException::class, UnauthorizedItemUpdateRequestException::class, ItemAlreadyClosedException::class)
    @Transactional
    fun closeItem(id: Long): ItemResponse {
        val item = itemRepository.findByIdOrThrow(id, ::ItemNotFoundException)

        if (item.cannotBeUpdatedByCurrentlyLoggedUser()) throw UnauthorizedItemUpdateRequestException(id)
        if (item.closed) throw ItemAlreadyClosedException(id)

        return itemRepository.save(item.apply {
            closedOn = LocalDateTime.now()
        }).toResponse()
    }

    private fun ItemAddRequest.mapToItem(): Item {
        // fields or many to one associations
        val title = this.title
        val description = this.description
        val price = this.price
        val addedBy = userRepository.findByIdOrThrow(this.addedBy, ::NewItemUserNotFoundException)
        val category = categoryRepository.findByIdOrThrow(this.category, ::NewItemCategoryNotFoundException)
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

    private fun Item.toResponse(): ItemResponse = ItemResponse(
            this.id,
            this.title,
            this.description,
            this.price,
            with(this.addedBy) { ItemResponse.ItemAddedByResponse(id, username) },
            this.addedOn,
            with(this.category) { ItemResponse.ItemCategoryResponse(id, name) },
            this.usedStatus,
            this.photos.map { photo -> photo.url },
            this.closedOn
    )

    private fun ItemUpdateRequest.mapToItem(oldItem: Item): Item {
        photoRepository.deleteAll(oldItem.photos)

        val title = this.title
        val description = this.description
        val price = this.price
        val addedBy = userRepository.findByIdOrThrow(oldItem.addedBy.id, ::NewItemUserNotFoundException)
        val category = categoryRepository.findByIdOrThrow(this.category, ::NewItemCategoryNotFoundException)
        val usedStatus = this.usedStatus

        val itemSavedPrePhotos = itemRepository.save(oldItem.apply {
            this.title = title
            this.description = description
            this.price = price
            this.addedBy = addedBy
            this.category = category
            this.usedStatus = usedStatus
        })

        val itemPhotos = this.photos.map {
            photoRepository.save(ItemPhoto(it, itemSavedPrePhotos))
        }.toMutableList()

        return itemSavedPrePhotos.apply {
            this.photos = itemPhotos
        }
    }

    private fun Item.cannotBeUpdatedByCurrentlyLoggedUser(): Boolean {
        val loggedInUsername = SecurityContextHolder.getContext().authentication.name
        return this.addedBy.username != loggedInUsername
    }
}
