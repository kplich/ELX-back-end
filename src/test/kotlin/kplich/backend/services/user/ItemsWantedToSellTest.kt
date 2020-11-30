package kplich.backend.services.user

import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.configurations.security.WithMockIdUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.transaction.annotation.Transactional

class ItemsWantedToSellTest : UserItemsTest() {

    @Test
    @WithMockIdUser(id = 4, "NewKidOnTheBlock")
    @Transactional
    fun `user's items are loaded correctly`() {
        val expectedItems = itemsWantedToSell.values.toList().map { item ->
            item.copy(
                    conversations = item.conversations.sortedBy { it.id }
            )
        }

        val foundItems = userItemsService.getItemsWantedToSell().map { item ->
            item.copy(
                    conversations = item.conversations.sortedBy { it.id }
            )
        }

        assertThat(foundItems).containsExactlyInAnyOrderElementsOf(expectedItems)
    }

    @Test
    fun `items are not loaded when no one's logged in`() {
        assertThrows<NoUserLoggedInException> {
            userItemsService.getItemsWantedToSell()
        }
    }
}
