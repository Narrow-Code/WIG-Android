package wig.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class borrowerCheckedOutResponse(
    val message: String,
    val success: Boolean,
    val ownerships: List<String>
)