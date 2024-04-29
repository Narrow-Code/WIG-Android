package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class NewOwnershipRequest(
    val qr: String,
    val name: String,
)