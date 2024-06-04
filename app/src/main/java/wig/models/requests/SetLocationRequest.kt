package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SetLocationRequest(
    val locationUID: String
)