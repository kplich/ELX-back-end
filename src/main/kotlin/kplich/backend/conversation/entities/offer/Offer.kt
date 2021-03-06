package kplich.backend.conversation.entities.offer

import kplich.backend.configurations.PricePrecisionConstants
import kplich.backend.authentication.entities.ApplicationUser
import kplich.backend.conversation.entities.Conversation
import kplich.backend.conversation.entities.Message
import kplich.backend.conversation.payloads.responses.offer.DoubleAdvanceOfferResponse
import kplich.backend.conversation.payloads.responses.offer.OfferResponse
import kplich.backend.conversation.payloads.responses.offer.PlainAdvanceOfferResponse
import kplich.backend.items.entities.Item
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "offers")
abstract class Offer(

        @OneToOne(mappedBy = "offer")
        var message: Message,

        @get:NotNull(message = PricePrecisionConstants.PRICE_REQUIRED_MSG)
        @get:DecimalMin(value = PricePrecisionConstants.PRICE_MINIMUM_STRING, inclusive = true, message = PricePrecisionConstants.PRICE_TOO_LOW_MSG)
        @get:DecimalMax(value = PricePrecisionConstants.PRICE_MAXIMUM_STRING, inclusive = true, message = PricePrecisionConstants.PRICE_TOO_HIGH_MSG)
        @get:Digits(integer = PricePrecisionConstants.PRICE_INTEGER_PART, fraction = PricePrecisionConstants.PRICE_DECIMAL_PART, message = PricePrecisionConstants.PRICE_TOO_PRECISE_MSG)
        var price: BigDecimal,

        @Enumerated(EnumType.STRING)
        var offerStatus: OfferStatus = OfferStatus.AWAITING,

        var contractAddress: String? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
) {

    @get:Transient
    val isAccepted
        get(): Boolean = this.offerStatus == OfferStatus.ACCEPTED

    @get:Transient
    val isDeclined
        get(): Boolean = this.offerStatus == OfferStatus.DECLINED

    @get:Transient
    val isCancelled
        get(): Boolean = this.offerStatus == OfferStatus.CANCELLED

    @get:Transient
    val isAwaiting
        get(): Boolean = this.offerStatus == OfferStatus.AWAITING

    @get:Transient
    val sender
        get(): ApplicationUser = this.message.sender

    @get:Transient
    val conversation
        get(): Conversation = this.message.conversation

    @get:Transient
    val item
        get(): Item = this.conversation.item

    fun cancel(): Offer {
        if(isAwaiting) {
            this.offerStatus = OfferStatus.CANCELLED
        } else {
            throw IllegalStateException("Cannot cancel accepted/denied/cancelled offer")
        }

        return this
    }

    fun accept(contractAddress: String): Offer {
        if (isAwaiting) {
            this.contractAddress = contractAddress
            this.offerStatus = OfferStatus.ACCEPTED
        } else {
            throw IllegalStateException("Cannot accept accepted/denied/cancelled offer")
        }

        return this
    }

    fun decline(): Offer {
        if (isAwaiting) {
            this.offerStatus = OfferStatus.DECLINED
        } else {
            throw IllegalStateException("Cannot decline accepted/denied/cancelled offer")
        }

        return this
    }

    fun toResponse(): OfferResponse {
        return when(this) {
            is PlainAdvanceOffer -> PlainAdvanceOfferResponse(
                    advance = advance,
                    id = id,
                    price = price,
                    offerStatus = offerStatus,
                    contractAddress = contractAddress
            )
            is DoubleAdvanceOffer -> DoubleAdvanceOfferResponse(
                    id = id,
                    price = price,
                    offerStatus = offerStatus,
                    contractAddress = contractAddress
            )
            else -> throw IllegalArgumentException("Cannot turn into offer response")
        }
    }
}

enum class OfferStatus {
    AWAITING, CANCELLED, ACCEPTED, DECLINED
}