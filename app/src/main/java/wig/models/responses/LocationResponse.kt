package wig.models.responses

import kotlinx.serialization.Serializable
import wig.models.entities.Location

@Serializable
data class LocationResponse(
    val message: String,
    val success: Boolean,
    val location: Location
)
