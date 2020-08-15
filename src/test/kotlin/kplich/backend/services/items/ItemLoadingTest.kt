package kplich.backend.services.items

import kplich.backend.exceptions.items.ItemNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.annotation.DirtiesContext

/**
 * Because these tests depend heavily on the state of data,
 * the container should be restarted before these tests.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ItemLoadingTest : ItemTest() {

    @Test
    fun `categories are loaded correctly`() {
        val expectedCategories = categories.values

        val foundCategories = this.itemService.getCategories()

        assertThat(foundCategories).containsExactlyInAnyOrderElementsOf(expectedCategories)
    }

    @Test
    fun `open items are loaded correctly`() {
        val expectedItems = items.values.filter { response -> response.closedOn == null }

        val foundItems = this.itemService.getAllOpenItems(null).toTypedArray()

        assertThat(foundItems).containsExactlyInAnyOrderElementsOf(expectedItems)
    }

    @Test
    fun `added items are loaded correctly`() {
        items.entries.forEach {
            assertThat(itemService.getItem(it.key)).isEqualTo(it.value)
        }
    }

    @Test
    fun `non-existing item cannot be loaded`() {
        assertThrows<ItemNotFoundException> {
            this.itemService.getItem(1000)
        }
    }
}
