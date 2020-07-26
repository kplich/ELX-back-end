package kplich.backend.entities

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.constraints.*
import kotlin.reflect.KClass

@Entity
@Table(name = "items")
@ClosedAfterAdded
data class Item(
        @NotBlank(message = TITLE_REQURIED_MSG)
        @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_MSG)
        var title: String,

        @NotBlank(message = DESCRIPTION_REQUIRED_MSG)
        @Size(min = DESCRIPTION_MIN_LENGTH, max = DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_LENGTH_MSG)
        var description: String,

        @NotNull(message = PRICE_REQUIRED_MSG)
        @DecimalMin(value = "0.0", inclusive = true, message = PRICE_TOO_LOW_MSG)
        @DecimalMax(value = "100000000.0", inclusive = true, message = PRICE_TOO_HIGH_MSG)
        @Digits(integer = 9, fraction = 4, message = PRICE_TOO_PRECISE_MSG)
        var price: BigDecimal,

        @NotNull(message = ADDING_USER_REQUIRED_MSG)
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        var addedBy: ApplicationUser,

        @NotNull(message = CATEGORY_REQUIRED_MSG)
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        var category: Category,

        @NotNull(message = STATUS_REQUIRED_MSG)
        @Enumerated(EnumType.STRING)
        var usedStatus: UsedStatus,

        @NotNull(message = PHOTOS_REQUIRED_MSG)
        @OneToMany(mappedBy = "item")
        var photos: MutableList<ItemPhoto>,

        @NotNull(message = ADDED_DATE_REQUIRED_MSG)
        @Size(min = 0, max = 8, message = PHOTOS_SIZE_MSG)
        var added: LocalDateTime = LocalDateTime.now(),

        var closed: LocalDateTime? = null,

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0
        ) {
    companion object {
        const val TITLE_REQURIED_MSG = "Title is required."
        const val TITLE_MIN_LENGTH = 10
        const val TITLE_MAX_LENGTH = 80
        const val TITLE_LENGTH_MSG = "Title must be between $TITLE_MIN_LENGTH and $TITLE_MAX_LENGTH characters long."

        const val DESCRIPTION_REQUIRED_MSG = "Description is required."
        const val DESCRIPTION_MIN_LENGTH = 25
        const val DESCRIPTION_MAX_LENGTH = 5000
        const val DESCRIPTION_LENGTH_MSG = "Description must be between $DESCRIPTION_MIN_LENGTH and $DESCRIPTION_MAX_LENGTH characters long."

        const val PRICE_REQUIRED_MSG = "Price is required."
        const val PRICE_TOO_LOW_MSG = "The lowest price allowed is 0 Ξ."
        const val PRICE_TOO_HIGH_MSG = "The highest price allowed is 100000000 Ξ."
        const val PRICE_TOO_PRECISE_MSG = "Price should have precision of 0.0001 Ξ."

        const val ADDING_USER_REQUIRED_MSG = "Adding user is required."

        const val CATEGORY_REQUIRED_MSG = "Category is required."

        const val STATUS_REQUIRED_MSG = "Status is required."

        const val ADDED_DATE_REQUIRED_MSG = "Added date is required."

        const val PHOTOS_REQUIRED_MSG = "Photo list mustn't be null."
        const val PHOTOS_SIZE_MSG = "Item can have at most 8 photos."
    }
}

@Suppress("unused") // parameters required by constraint validation?
@Constraint(validatedBy = [ClosedAfterAddedValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ClosedAfterAdded(val message: String = "", val groups: Array<KClass<*>> = [], val payload: Array<KClass<out Payload>> = [])

class ClosedAfterAddedValidator : ConstraintValidator<ClosedAfterAdded, Item> {
    override fun isValid(item: Item, context: ConstraintValidatorContext): Boolean {
        return item.closed == null || item.closed!!.isAfter(item.added)
    }
}

@Entity
@Table(name = "categories")
data class Category(
        @NotBlank var name: String,
        @Id var id: Int
)

enum class UsedStatus {
    NEW, USED, NOT_APPLICABLE
}

@Entity
@Table(name = "item_photos")
data class ItemPhoto(
        @NotNull @NotBlank var url: String,
        @ManyToOne var item: Item,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0
)
