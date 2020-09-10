package kplich.backend.services.items

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.entities.items.UsedStatus
import kplich.backend.exceptions.items.BadEditItemRequestException
import kplich.backend.exceptions.items.UnauthorizedItemAddingRequestException
import kplich.backend.exceptions.items.UserWithIdNotFoundException
import kplich.backend.payloads.requests.items.ItemAddRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import javax.validation.ValidationException

class ItemCreatingTest : ItemTest() {

    @Test
    @WithMockIdUser(id = 1, username = "kplich")
    fun `item can be created`() {
        val request = ItemAddRequest(
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 1,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )

        val itemResponse = this.itemService.addItem(request)

        assertThat(itemResponse.title).isEqualTo(request.title)
        assertThat(itemResponse.description).isEqualTo(request.description)
        assertThat(itemResponse.price).isEqualTo(request.price)
        assertThat(itemResponse.category).isEqualTo(categories[request.category])
        assertThat(itemResponse.usedStatus).isEqualTo(request.usedStatus)
        assertThat(itemResponse.photoUrls).isEqualTo(request.photos)
        assertThat(itemResponse.addedBy.username).isEqualTo("kplich")
    }

    @Test
    fun `item cannot be created when user isn't logged in`() {
        val request = ItemAddRequest(
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 1,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )

        assertThrows<UnauthorizedItemAddingRequestException> {
            this.itemService.addItem(request)
        }
    }

    @Test
    @WithMockIdUser(id = 1000, username = "doesnt_exist")
    fun `item cannot be created by user that doesn't exist`() {
        val request = ItemAddRequest(
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 1,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )


        assertThrows<UserWithIdNotFoundException> {
            this.itemService.addItem(request)
        }
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich")
    fun `item cannot be created with wrong price`() {
        val wrongPrices = arrayOf(
                BigDecimal("-0.235"),
                BigDecimal("0.2345678"),
                BigDecimal("1351351353215321.444")
        )

        wrongPrices.forEach {
            val request = ItemAddRequest(
                    title = "Title for newly added item",
                    description = "Description for newly added item",
                    price = it,
                    category = 1,
                    usedStatus = UsedStatus.NEW,
                    photos = listOf("photo1", "photo2")
            )

            assertThrows<ValidationException> {
                itemService.addItem(request)
            }
        }
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich")
    fun `item cannot be created with non-existent category`() {
        val request = ItemAddRequest(
                title = "Title for newly added item",
                description = "Description for newly added item",
                price = BigDecimal("999.999"),
                category = 1000000,
                usedStatus = UsedStatus.NEW,
                photos = listOf("photo1", "photo2")
        )

        assertThrows<BadEditItemRequestException> {
            itemService.addItem(request)
        }
    }
}
