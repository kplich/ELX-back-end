package kplich.backend.services.user

import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.user.payloads.responses.items.ItemWantedToSellResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ItemsWantedToSellTest : UserItemsTest() {

    @Test
    @WithMockIdUser(id = 4, "NewKidOnTheBlock")
    fun `user's items are loaded correctly`() {
        val expectedItems = itemsWantedToSell.values

        val foundItems = userItemsService.getItemsWantedToSell().toTypedArray()

        assertThat(foundItems).containsExactlyInAnyOrderElementsOf(expectedItems)
    }

    @Test
    fun `items are not loaded when no one's logged in`() {
        assertThrows<NoUserLoggedInException> {
            userItemsService.getItemsWantedToSell()
        }
    }
}