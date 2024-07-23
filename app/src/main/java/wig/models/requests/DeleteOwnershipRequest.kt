package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteOwnershipRequest(
    val ownershipUID: String
)