package kplich.backend.services.user

import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.configurations.security.WithMockIdUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.transaction.Transactional

class ItemsWantedToBuyTest: UserItemsTest() {

    @Test
    @WithMockIdUser(id = 4, "NewKidOnTheBlock")
    @Transactional
    fun `user's items are loaded correctly`() {
        val expectedItems = itemsWantedToBuy.values.toList()

        val foundItems = userItemsService.getItemsWantedToBuy()

        assertThat(foundItems).containsExactlyInAnyOrderElementsOf(expectedItems)
    }

    @Test
    fun `items are not loaded when no one's logged in`() {
        assertThrows<NoUserLoggedInException> {
            userItemsService.getItemsWantedToBuy()
        }
    }
}
