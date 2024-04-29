package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponse(
    val message: String,
    val success: Boolean,
    val ownerships: List<String>
)