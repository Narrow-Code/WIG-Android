package wig.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val locationUID: Int,
    val locationOwner: Int,
    val locationName: String,
    val locationParent: Int,
    val locationQR: String,
    val locationTags: String,
    val locationDescription: String,
    val user: User,
    val location: Location?
)