package wig.models.entities

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val locationUID: String,
    val locationOwner: String,
    var locationName: String,
    val locationParent: String,
    val locationQR: String,
    val locationTags: String,
    val locationDescription: String,
    val user: User,
    var location: Location?
)