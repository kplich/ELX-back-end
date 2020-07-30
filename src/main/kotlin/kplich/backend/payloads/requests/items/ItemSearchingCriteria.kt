package kplich.backend.payloads.requests.items

import kplich.backend.entities.Item
import kplich.backend.entities.UsedStatus
import java.math.BigDecimal
import java.util.function.Predicate
import java.util.regex.Pattern
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.Size

data class ItemSearchingCriteria (
        val searchQuery: String?,
        val category: Int?,

        @get:DecimalMin(value = "0.0", inclusive = true, message = Item.PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = "100000000.0", inclusive = true, message = Item.PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = 9, fraction = 4, message = Item.PRICE_TOO_PRECISE_MSG)
        val minimalPrice: BigDecimal?,

        @get:DecimalMin(value = "0.0", inclusive = true, message = Item.PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = "100000000.0", inclusive = true, message = Item.PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = 9, fraction = 4, message = Item.PRICE_TOO_PRECISE_MSG)
        val maximalPrice: BigDecimal?,

        @get:Size(min = 1)
        val statuses: List<UsedStatus>?
) {
    private val containsWords get(): Predicate<Item> = Predicate { t ->
        searchQuery?.split(WHITESPACE_PATTERN)?.any { word ->
            t.title.toLowerCase().contains(word.toLowerCase())
                    || t.description.toLowerCase().contains(word.toLowerCase())
        } ?: true
    }

    private val isInCategory get(): Predicate<Item> = Predicate { t ->
        category?.equals(t.category.id) ?: true
    }

    private val isInPriceRange get(): Predicate<Item> = Predicate { t ->
        val moreThanMinimal = if(minimalPrice != null) { minimalPrice <= t.price } else true
        val lessThanMaximal = if(maximalPrice != null) { maximalPrice >= t.price } else true

        return@Predicate moreThanMinimal && lessThanMaximal
    }

    private val hasProvidedStatus get(): Predicate<Item> = Predicate { t ->
        statuses?.any { status -> t.usedStatus == status || t.usedStatus == UsedStatus.NOT_APPLICABLE } ?: true
    }

    fun testItemForFilters(item: Item): Boolean {
        return with(item) { containsWords.test(this)
                    && isInCategory.test(this)
                    && isInPriceRange.test(this)
                    && hasProvidedStatus.test(this)
        }
    }

    companion object {
        private val WHITESPACE_PATTERN = Pattern.compile("(?U)\\s+")

        val isNotClosed get(): Predicate<Item> = Predicate {
            !it.closed
        }
    }
}
