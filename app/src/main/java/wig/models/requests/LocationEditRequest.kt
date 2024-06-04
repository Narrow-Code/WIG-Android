package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class LocationEditRequest(
    val locationUID: String,
    val locationName: String,
    val locationDescription: String,
    val locationTags: String,
    val qr: String
)