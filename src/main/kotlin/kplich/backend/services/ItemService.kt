package kplich.backend.services

import kplich.backend.entities.Item
import kplich.backend.entities.ItemPhoto
import kplich.backend.exceptions.*
import kplich.backend.payloads.requests.ItemAddRequest
import kplich.backend.payloads.requests.ItemUpdateRequest
import kplich.backend.payloads.responses.ItemSimpleResponse
import kplich.backend.repositories.*
import org.modelmapper.ModelMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime

// TODO: improve model mapping

@Service
class ItemService(
        private val modelMapper: ModelMapper,
        private val itemRepository: ItemRepository,
        private val userRepository: ApplicationUserRepository,
        private val categoryRepository: CategoryRepository,
        private val photoRepository: PhotoRepository
) {

    @Throws(
            ItemNotFoundException::class,
            UnauthorizedItemUpdateRequestException::class,
            NewItemCategoryNotFound::class)
    @Transactional
    fun updateItem(request: ItemUpdateRequest): ItemSimpleResponse {
        val oldItem = itemRepository.findByIdOrThrow(request.id, ::ItemNotFoundException)

        if (oldItem.canBeUpdatedByCurrentlyLoggedUser()) throw UnauthorizedItemUpdateRequestException(oldItem.id)

        if (oldItem.closed) throw ClosedItemUpdateException()

        val categoryEntity = categoryRepository.findByIdOrThrow(request.category, ::NewItemCategoryNotFound)

        photoRepository.deleteAll(oldItem.photos)

        val newItemPrePhotos = itemRepository.save(
                oldItem.apply {
                    title = request.title
                    description = request.description
                    price = request.price
                            .toBigDecimal(MathContext(13)).setScale(4, RoundingMode.HALF_UP)
                    category = categoryEntity
                    usedStatus = request.usedStatus
                }
        )

        val newItemPhotos = request.photos.map { url ->
            photoRepository.save(
                    ItemPhoto(url, newItemPrePhotos)
            )
        }.toMutableList()

        val newItem = itemRepository.save(
                newItemPrePhotos.apply {
                    photos = newItemPhotos
                }
        )

        return newItem.toResponse()
    }

    @Throws(ItemNotFoundException::class, UnauthorizedItemUpdateRequestException::class, ItemAlreadyClosedException::class)
    @Transactional
    fun closeItem(id: Long): ItemSimpleResponse {
        val item = itemRepository.findByIdOrThrow(id, ::ItemNotFoundException)

        if (item.canBeUpdatedByCurrentlyLoggedUser()) throw UnauthorizedItemUpdateRequestException(id)

        if (item.closed) throw ItemAlreadyClosedException(id)

        return itemRepository.save(
                item.apply {
                    closedOn = LocalDateTime.now()
                }
        ).toResponse()
    }

    @Throws(ItemNotFoundException::class)
    fun getItem(id: Long): ItemSimpleResponse {
        val item = itemRepository.findByIdOrThrow(id, ::ItemNotFoundException)

        return item.toResponse()
    }

    @Throws(BadAddItemRequestException::class)
    @Transactional
    fun addItem(request: ItemAddRequest): ItemSimpleResponse {
        val addedByUserEntity = userRepository.findByIdOrThrow(request.addedBy, ::ItemAddingUserNotFound)

        val categoryEntity = categoryRepository.findByIdOrThrow(request.category, ::NewItemCategoryNotFound)

        val mappedEntity: Item = modelMapper.map(request, Item::class.java)
        val newItemEntity = mappedEntity.apply {
            addedBy = addedByUserEntity
            category = categoryEntity
        }

        val savedItemEntity = itemRepository.save(newItemEntity)

        val itemPhotos = request.photos.map { url ->
            photoRepository.save(
                    ItemPhoto(url, savedItemEntity)
            )
        }.toMutableList()

        val updatedItemEntity = itemRepository.save(
                savedItemEntity.apply {
                    photos = itemPhotos
                }
        )

        return updatedItemEntity.toResponse()
    }

    private fun Item.toResponse(): ItemSimpleResponse = ItemSimpleResponse(
                                                    this.title,
                                                    this.description,
                                                    this.price.toFloat(),
                                                    this.addedBy.username,
                                                    this.addedOn,
                                                    this.category.name,
                                                    this.usedStatus.name,
                                                    this.photos.map { photo -> photo.url },
                                                    this.closedOn)

    private fun Item.canBeUpdatedByCurrentlyLoggedUser(): Boolean {
        val loggedInUsername = SecurityContextHolder.getContext().authentication.name
        return this.addedBy.username == loggedInUsername
    }
}
