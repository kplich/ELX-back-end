package kplich.backend.conversation.payloads.requests.offer

import java.math.BigDecimal

class NewDoubleAdvanceOfferRequest(
        price: BigDecimal
) : NewOfferRequest(price)