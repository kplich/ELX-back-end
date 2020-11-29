package kplich.backend.services.user

import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.user.payloads.responses.items.ItemSoldResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ItemsSoldTest: UserItemsTest() {
    @Test
    @WithMockIdUser(id = 4, "kplich4")
    @Disabled
    fun `user's items are loaded correctly`() {
        val expectedItems = emptyList<ItemSoldResponse>()

        val foundItems = userItemsService.getItemsSold().toTypedArray()

        Assertions.assertThat(foundItems).containsExactlyInAnyOrderElementsOf(expectedItems)
    }

    @Test
    fun `items are not loaded when no one's logged in`() {
        assertThrows<NoUserLoggedInException> {
            userItemsService.getItemsSold()
        }
    }
}