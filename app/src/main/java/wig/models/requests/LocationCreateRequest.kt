package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class LocationCreateRequest(
    val locationName: String,
    val locationQR: String
)