package kplich.backend.services.user

import kplich.backend.authentication.NoUserLoggedInException
import kplich.backend.configurations.security.WithMockIdUser
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.transaction.Transactional

class ItemsBoughtTest: UserItemsTest() {
    @Test
    @WithMockIdUser(id = 4, "NewKidOnTheBlock")
    @Transactional
    fun `user's items are loaded correctly`() {
        val expectedItems = itemsBought.values.toList()

        val foundItems = userItemsService.getItemsBought()

        Assertions.assertThat(foundItems).containsExactlyInAnyOrderElementsOf(expectedItems)
    }

    @Test
    fun `items are not loaded when no one's logged in`() {
        assertThrows<NoUserLoggedInException> {
            userItemsService.getItemsBought()
        }
    }
}
