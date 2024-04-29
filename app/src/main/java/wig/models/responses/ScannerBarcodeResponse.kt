package wig.models.responses

import kotlinx.serialization.Serializable
import wig.models.entities.Ownership

@Serializable
data class ScannerBarcodeResponse(
    val message: String,
    val success: Boolean,
    val ownership: List<Ownership>
)
