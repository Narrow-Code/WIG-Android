package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class OwnershipCreateRequest(
    val qr: String,
    val name: String,
)