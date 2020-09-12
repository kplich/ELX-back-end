package kplich.backend.entities.items

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import kplich.backend.configurations.PricePrecisionConstants.PRICE_DECIMAL_PART
import kplich.backend.configurations.PricePrecisionConstants.PRICE_INTEGER_PART
import kplich.backend.configurations.PricePrecisionConstants.PRICE_MAXIMUM_STRING
import kplich.backend.configurations.PricePrecisionConstants.PRICE_MINIMUM_STRING
import kplich.backend.configurations.PricePrecisionConstants.PRICE_REQUIRED_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_HIGH_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_LOW_MSG
import kplich.backend.configurations.PricePrecisionConstants.PRICE_TOO_PRECISE_MSG
import kplich.backend.entities.authentication.ApplicationUser
import kplich.backend.entities.conversation.Conversation
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
@ClosedAfterAdded(message = Item.CLOSED_AFTER_ADDED_MSG)
data class Item(
        @get:NotBlank(message = TITLE_REQURIED_MSG)
        @get:Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_LENGTH_MSG)
        var title: String,

        @get:NotBlank(message = DESCRIPTION_REQUIRED_MSG)
        @get:Size(min = DESCRIPTION_MIN_LENGTH, max = DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_LENGTH_MSG)
        var description: String,

        @get:NotNull(message = PRICE_REQUIRED_MSG)
        @get:DecimalMin(value = PRICE_MINIMUM_STRING, inclusive = true, message = PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = PRICE_MAXIMUM_STRING, inclusive = true, message = PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = PRICE_INTEGER_PART, fraction = PRICE_DECIMAL_PART, message = PRICE_TOO_PRECISE_MSG)
        var price: BigDecimal,

        @get:NotNull(message = ADDING_USER_REQUIRED_MSG)
        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "added_by_id")
        var addedBy: ApplicationUser,

        @get:NotNull(message = CATEGORY_REQUIRED_MSG)
        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "category_id")
        var category: Category,

        @get:NotNull(message = STATUS_REQUIRED_MSG)
        @Enumerated(EnumType.STRING)
        var usedStatus: UsedStatus,

        @get:NotNull(message = PHOTOS_REQUIRED_MSG)
        @get:Size(min = PHOTOS_MINIMAL_SIZE, max = PHOTOS_MAXIMAL_SIZE, message = PHOTOS_SIZE_MSG)
        @OneToMany(mappedBy = "item", targetEntity = ItemPhoto::class)
        @JsonManagedReference
        var photos: MutableList<ItemPhoto>,

        @get:NotNull(message = ADDED_DATE_REQUIRED_MSG)
        var addedOn: LocalDateTime = LocalDateTime.now(),

        var closedOn: LocalDateTime? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
        ) {

    @get:NotNull
    @OneToMany(mappedBy = "item")
    var conversations: MutableList<Conversation> = mutableListOf()

    @get:Transient
    val closed get(): Boolean = closedOn != null

    fun close(): Item {
        if(closed) {
            throw IllegalStateException("Cannot close an already closed item")
        }
        else {
            closedOn = LocalDateTime.now()
        }

        return this
    }

    companion object {
        const val TITLE_REQURIED_MSG = "Title is required."
        const val TITLE_MIN_LENGTH = 10
        const val TITLE_MAX_LENGTH = 80
        const val TITLE_LENGTH_MSG = "Title must be between $TITLE_MIN_LENGTH and $TITLE_MAX_LENGTH characters long."

        const val DESCRIPTION_REQUIRED_MSG = "Description is required."
        const val DESCRIPTION_MIN_LENGTH = 25
        const val DESCRIPTION_MAX_LENGTH = 5000
        const val DESCRIPTION_LENGTH_MSG = "Description must be between $DESCRIPTION_MIN_LENGTH and $DESCRIPTION_MAX_LENGTH characters long."

        const val ADDING_USER_REQUIRED_MSG = "Adding user is required."

        const val CATEGORY_REQUIRED_MSG = "Category is required."

        const val STATUS_REQUIRED_MSG = "Status is required."

        const val ADDED_DATE_REQUIRED_MSG = "Added date is required."

        const val PHOTOS_REQUIRED_MSG = "Photo list mustn't be null."
        const val PHOTOS_MINIMAL_SIZE = 0
        const val PHOTOS_MAXIMAL_SIZE = 8
        const val PHOTOS_SIZE_MSG = "Item can have at most 8 photos."

        const val CLOSED_AFTER_ADDED_MSG = "Item can be closed only after it has been added"
    }
}

@Suppress("unused") // parameters required by constraint validation?
@Constraint(validatedBy = [ClosedAfterAddedValidator::class])
@Target(AnnotationTarget.CLASS)
annotation class ClosedAfterAdded(val message: String = "", val groups: Array<KClass<*>> = [], val payload: Array<KClass<out Payload>> = [])

class ClosedAfterAddedValidator : ConstraintValidator<ClosedAfterAdded, Item> {
    override fun isValid(item: Item, context: ConstraintValidatorContext): Boolean {
        return item.closedOn == null || item.closedOn!!.isAfter(item.addedOn)
    }
}

@Entity
@Table(name = "categories")
data class Category(
        @get:NotBlank
        var name: String,

        @Id
        var id: Int
)

enum class UsedStatus {
    NEW, USED, NOT_APPLICABLE
}

@Entity
@Table(name = "item_photos")
data class ItemPhoto(
        @get:NotNull
        @get:NotBlank
        var url: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JsonBackReference
        var item: Item,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
)