package kplich.backend.configurations

object PricePrecisionConstants {
    const val PRICE_INTEGER_PART = 9
    const val PRICE_DECIMAL_PART = 4

    const val PRICE_REQUIRED_MSG = "Price is required."
    const val PRICE_MINIMUM_STRING = "0.0"
    const val PRICE_TOO_LOW_MSG = "The lowest price allowed is 0 Ξ."
    const val PRICE_MAXIMUM_STRING = "100000000.0"
    const val PRICE_TOO_HIGH_MSG = "The highest price allowed is 100000000 Ξ."
    const val PRICE_TOO_PRECISE_MSG = "Price should have precision no smaller than 0.0001 Ξ."

    const val ADVANCE_REQUIRED_MSG = "Advance is required."
    const val ADVANCE_TOO_LOW_MSG = "The lowest advance allowed is 0 Ξ."
    const val ADVANCE_TOO_HIGH_MSG = "The highest advance allowed is 100000000 Ξ."
    const val ADVANCE_TOO_PRECISE_MSG = "Advance should have precision no smaller than 0.0001 Ξ."
}
