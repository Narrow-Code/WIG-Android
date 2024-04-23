package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequest(
    val ownerships: List<String>
)