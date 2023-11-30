package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Location

@Serializable
data class LocationResponse(
    val message: String,
    val success: Boolean,
    val location: Location
)
