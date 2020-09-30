package kplich.backend.services.items

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.entities.items.UsedStatus
import kplich.backend.exceptions.items.BadEditItemRequestException
import kplich.backend.exceptions.items.ClosedItemUpdateException
import kplich.backend.exceptions.items.ItemNotFoundException
import kplich.backend.exceptions.items.UnauthorizedItemUpdateRequestException
import kplich.backend.payloads.requests.items.ItemUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.transaction.TransactionSystemException
import java.math.BigDecimal

/**
 * Item with id=3 is updated here.
 */
class ItemUpdatingTest : ItemTest() {

    @Test
    @WithMockIdUser(id = 3, username = "kplich3")
    fun `item can be updated`() {
        val request = ItemUpdateRequest(
                id = 3,
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 3,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )

        val itemResponse = this.itemService.updateItem(request)

        assertThat(itemResponse.title).isEqualTo(request.title)
        assertThat(itemResponse.description).isEqualTo(request.description)
        assertThat(itemResponse.price).isEqualTo(request.price)
        assertThat(itemResponse.category).isEqualTo(categories[request.category])
        assertThat(itemResponse.usedStatus).isEqualTo(request.usedStatus)
        assertThat(itemResponse.photoUrls).isEqualTo(request.photos)
        assertThat(itemResponse.addedBy.username).isEqualTo("kplich3")
    }

    @Test
    fun `item cannot be updated when user isn't logged in`() {
        val request = ItemUpdateRequest(
                id = 3,
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 3,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )

        assertThrows<UnauthorizedItemUpdateRequestException> {
            this.itemService.updateItem(request)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `item cannot be updated by user that didn't create it`() {
        val request = ItemUpdateRequest(
                id = 3,
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 3,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )

        assertThrows<UnauthorizedItemUpdateRequestException> {
            this.itemService.updateItem(request)
        }
    }

    @Test
    @WithMockIdUser(id = 3, username = "kplich3")
    fun `item cannot be updated with wrong price`() {
        val wrongPrices = arrayOf(
                BigDecimal("-0.235"),
                BigDecimal("0.2345678"),
                BigDecimal("1351351353215321.444")
        )

        wrongPrices.forEach {
            val request = ItemUpdateRequest(
                    id = 3,
                    title = "Title for newly added item",
                    description = "Description for newly added item",
                    price = it,
                    category = 3,
                    usedStatus = UsedStatus.NEW,
                    photos = listOf("photo1", "photo2")
            )

            // for some weird reason, ValidationException causes Spring to throw an extra
            // TransactionSystemException (it doesn't do the same in ItemAddingTest)
            assertThrows<TransactionSystemException> {
                itemService.updateItem(request)
            }
        }
    }

    @Test
    @WithMockIdUser(id = 3, username = "kplich3")
    fun `item cannot be updated with non-existent category`() {
        val request = ItemUpdateRequest(
                id = 3,
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 3000,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )

        assertThrows<BadEditItemRequestException> {
            itemService.updateItem(request)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `item cannot be updated after it's been closed`() {
        val request = ItemUpdateRequest(
                id = 2,
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 3,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )

        assertThrows<ClosedItemUpdateException> {
            itemService.updateItem(request)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `non-existent item cannot be updated`() {
        val request = ItemUpdateRequest(
                id = 10000000,
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 3,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )

        assertThrows<ItemNotFoundException> {
            itemService.updateItem(request)
        }
    }
}
