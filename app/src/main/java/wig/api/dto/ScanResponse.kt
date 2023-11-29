package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Ownership
import wig.models.User

/**
 * Represents a data class for the response of scanning a barcode.
 */
@Serializable
data class ScanResponse(
    val message: String,
    val success: Boolean,
    val ownership: List<Ownership>
)
