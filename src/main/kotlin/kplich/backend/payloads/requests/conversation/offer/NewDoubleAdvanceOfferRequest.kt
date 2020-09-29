package kplich.backend.payloads.requests.conversation.offer

import java.math.BigDecimal

class NewDoubleAdvanceOfferRequest(
        price: BigDecimal
) : NewOfferRequest(price)