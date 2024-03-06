package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Ownership

@Serializable
data class SearchOwnershipResponse(
    val message: String,
    val success: Boolean,
    val ownership: List<Ownership>
)
