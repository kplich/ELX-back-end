package kplich.backend.services.items

import kplich.backend.exceptions.ItemAlreadyClosedException
import kplich.backend.exceptions.ItemNotFoundException
import kplich.backend.exceptions.UnauthorizedItemUpdateRequestException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.test.context.support.WithMockUser

/**
 * Item with id=1 is closed here.
 */
class ItemClosingTest : ItemTest() {

    @Test
    fun `item cannot be closed when no one is logged in`() {
        assertThrows<UnauthorizedItemUpdateRequestException> {
            this.itemService.closeItem(1)
        }
    }

    @Test
    @WithMockUser(username = "kplich")
    fun `open item can be closed correctly`() {
        assertThat(this.itemService.closeItem(1).closedOn).isNotNull()
    }

    @Test
    @WithMockUser(username = "kplich2")
    fun `closed item cannot be closed`() {
        assertThrows<ItemAlreadyClosedException> {
            this.itemService.closeItem(2)
        }
    }

    @Test
    @WithMockUser(username = "kplich")
    fun `non-existing item cannot be closed`() {
        assertThrows<ItemNotFoundException> {
            this.itemService.closeItem(1000)
        }
    }

    @Test
    @WithMockUser(username = "kplich2")
    fun `no one but creator of the item can close it`() {
        assertThrows<UnauthorizedItemUpdateRequestException> {
            this.itemService.closeItem(1)
        }
    }
}
