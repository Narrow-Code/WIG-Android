package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Ownership
import wig.models.User

@Serializable
data class ScanResponse(
    val message: String,
    val success: Boolean,
    val ownership: List<Ownership>
)
