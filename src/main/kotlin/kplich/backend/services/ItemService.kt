package kplich.backend.services

import kplich.backend.entities.Item
import kplich.backend.entities.ItemPhoto
import kplich.backend.exceptions.BadAddItemRequestException
import kplich.backend.payloads.requests.ItemAddRequest
import kplich.backend.payloads.responses.ItemAddedResponse
import kplich.backend.repositories.ApplicationUserRepository
import kplich.backend.repositories.CategoryRepository
import kplich.backend.repositories.ItemRepository
import kplich.backend.repositories.PhotoRepository
import org.modelmapper.ModelMapper
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

    @Throws(BadAddItemRequestException::class)
    @Transactional
    fun addItem(itemAddRequest: ItemAddRequest): ItemAddedResponse {
        val addedByUserEntity = userRepository.findByUsername(itemAddRequest.addedBy)
                ?: throw BadAddItemRequestException("No user with username ${itemAddRequest.addedBy} found.")

        val categoryEntity = categoryRepository.findByName(itemAddRequest.category)
                ?: throw BadAddItemRequestException("No category ${itemAddRequest.category} found")

        val newItemEntity: Item = modelMapper.map(itemAddRequest, Item::class.java).apply {
            addedBy = addedByUserEntity
            added = LocalDateTime.now()
            category = categoryEntity
            closed = null
        }

        val savedItemEntity = itemRepository.save(newItemEntity)

        val itemPhotos = itemAddRequest.photoUrls.map { url ->
            photoRepository.save(
                    ItemPhoto(url, savedItemEntity)
            )
        }.toMutableList()

        val updatedItemEntity = itemRepository.save(
                savedItemEntity.apply {
                    photos = itemPhotos
                }
        )

        return with(updatedItemEntity) {
            ItemAddedResponse(
                    this.title,
                    this.description,
                    this.price.toFloat(),
                    this.addedBy.username,
                    this.added,
                    this.category.name,
                    this.usedStatus.name,
                    this.photos.map { photo -> photo.url },
                    this.closed
            )
        }
    }
}
