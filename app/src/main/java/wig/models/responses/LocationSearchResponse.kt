package wig.models.responses

import kotlinx.serialization.Serializable
import wig.models.entities.Location

@Serializable
data class LocationSearchResponse(
    val message: String,
    val success: Boolean,
    val locations: List<Location>
)
