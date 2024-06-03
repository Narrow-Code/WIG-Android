package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class OwnershipEditRequest(
    val ownershipUID: String,
    val customItemName: String,
    val customItemImg: String,
    val customItemDescription: String,
    val itemTags: String,
    val qr: String
)