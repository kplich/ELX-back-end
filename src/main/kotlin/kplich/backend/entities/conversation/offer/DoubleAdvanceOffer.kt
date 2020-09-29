package kplich.backend.entities.conversation.offer

import kplich.backend.entities.conversation.Message
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "double_advance_offers")
class DoubleAdvanceOffer(
        message: Message,
        price: BigDecimal
): Offer(message, price)