package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Location
import wig.models.Ownership

@Serializable
data class UnpackResponse(
    val message: String,
    val success: Boolean,
    val ownerships: List<Ownership>,
    val locations: List<Location>
)