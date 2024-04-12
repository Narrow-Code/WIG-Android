package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Location

@Serializable
data class SearchLocationResponse(
    val message: String,
    val success: Boolean,
    val locations: List<Location>
)
