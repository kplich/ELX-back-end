package kplich.backend.services

import kplich.backend.entities.Item
import kplich.backend.entities.ItemPhoto
import kplich.backend.exceptions.BadAddItemRequestException
import kplich.backend.exceptions.ItemAlreadyClosedException
import kplich.backend.exceptions.ItemNotFoundException
import kplich.backend.exceptions.UnauthorizedItemClosingRequest
import kplich.backend.payloads.requests.ItemAddRequest
import kplich.backend.payloads.responses.ItemResponse
import kplich.backend.repositories.ApplicationUserRepository
import kplich.backend.repositories.CategoryRepository
import kplich.backend.repositories.ItemRepository
import kplich.backend.repositories.PhotoRepository
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ItemService(
        private val modelMapper: ModelMapper,
        private val itemRepository: ItemRepository,
        private val userRepository: ApplicationUserRepository,
        private val categoryRepository: CategoryRepository,
        private val photoRepository: PhotoRepository
) {
    
    fun updateItem()

    @Throws(ItemNotFoundException::class, UnauthorizedItemClosingRequest::class, ItemAlreadyClosedException::class)
    @Transactional
    fun closeItem(id: Long): ItemResponse {
        val item = itemRepository.findByIdOrNull(id) ?: throw ItemNotFoundException(id)

        val loggedUser = SecurityContextHolder.getContext().authentication
        if (loggedUser.name != item.addedBy.username) throw UnauthorizedItemClosingRequest(loggedUser.name, id)

        if (item.closed != null) throw ItemAlreadyClosedException(id)

        return itemRepository.save(
                item.apply {
                    closed = LocalDateTime.now()
                }
        ).toResponse()
    }

    @Throws(ItemNotFoundException::class)
    fun getItem(id: Long): ItemResponse {
        val item = itemRepository.findByIdOrNull(id) ?: throw ItemNotFoundException(id)

        return item.toResponse()
    }

    @Throws(BadAddItemRequestException::class)
    @Transactional
    fun addItem(itemAddRequest: ItemAddRequest): ItemResponse {
        val addedByUserEntity = userRepository.findByIdOrNull(itemAddRequest.addedBy)
                ?: throw BadAddItemRequestException("No user with id ${itemAddRequest.addedBy} found.")

        val categoryEntity = categoryRepository.findByIdOrNull(itemAddRequest.category)
                ?: throw BadAddItemRequestException("No category with id ${itemAddRequest.category} found")

        val mappedEntity: Item = modelMapper.map(itemAddRequest, Item::class.java)
        val newItemEntity = mappedEntity.apply {
            addedBy = addedByUserEntity
            category = categoryEntity
        }

        val savedItemEntity = itemRepository.save(newItemEntity)

        val itemPhotos = itemAddRequest.photos.map { url ->
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

    private fun Item.toResponse(): ItemResponse =
            ItemResponse(
                this.title,
                this.description,
                this.price.toFloat(),
                this.addedBy.username,
                this.added,
                this.category.name,
                this.usedStatus.name,
                this.photos.map { photo -> photo.url },
                this.closed)
}
