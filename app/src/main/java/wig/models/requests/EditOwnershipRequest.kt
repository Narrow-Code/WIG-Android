package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class EditOwnershipRequest(
    val customItemName: String,
    val customItemImg: String,
    val customItemDescription: String,
    val itemTags: String,
    val qr: String
)