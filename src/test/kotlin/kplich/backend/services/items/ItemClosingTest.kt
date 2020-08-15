package kplich.backend.services.items

import kplich.backend.configurations.security.WithMockIdUser
import kplich.backend.exceptions.items.ItemAlreadyClosedException
import kplich.backend.exceptions.items.ItemNotFoundException
import kplich.backend.exceptions.items.UnauthorizedItemUpdateRequestException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
    @WithMockIdUser(id = 1, username = "kplich")
    fun `open item can be closed correctly`() {
        assertThat(this.itemService.closeItem(1).closedOn).isNotNull()
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `closed item cannot be closed`() {
        assertThrows<ItemAlreadyClosedException> {
            this.itemService.closeItem(2)
        }
    }

    @Test
    @WithMockIdUser(id = 1, username = "kplich")
    fun `non-existing item cannot be closed`() {
        assertThrows<ItemNotFoundException> {
            this.itemService.closeItem(1000)
        }
    }

    @Test
    @WithMockIdUser(id = 2, username = "kplich2")
    fun `no one but creator of the item can close it`() {
        assertThrows<UnauthorizedItemUpdateRequestException> {
            this.itemService.closeItem(1)
        }
    }
}
