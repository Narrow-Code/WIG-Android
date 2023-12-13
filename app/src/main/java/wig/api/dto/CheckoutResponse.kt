package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Borrower

@Serializable
data class CheckoutResponse(
    val message: String,
    val success: Boolean,
    val ownerships: List<Int>
)