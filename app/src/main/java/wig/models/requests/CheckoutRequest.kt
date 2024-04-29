package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequest(
    val ownerships: List<String>
)