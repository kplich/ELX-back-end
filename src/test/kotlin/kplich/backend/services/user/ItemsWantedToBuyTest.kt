package kplich.backend.services.user

import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.user.payloads.responses.items.ItemWantedToBuyResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ItemsWantedToBuyTest: UserItemsTest() {

    @Test
    @WithMockIdUser(id = 4, "kplich4")
    @Disabled
    fun `user's items are loaded correctly`() {
        val expectedItems = emptyList<ItemWantedToBuyResponse>()

        val foundItems = userItemsService.getItemsWantedToBuy().toTypedArray()

        assertThat(foundItems).containsExactlyInAnyOrderElementsOf(expectedItems)
    }

    @Test
    fun `items are not loaded when no one's logged in`() {
        assertThrows<NoUserLoggedInException> {
            userItemsService.getItemsWantedToBuy()
        }
    }
}