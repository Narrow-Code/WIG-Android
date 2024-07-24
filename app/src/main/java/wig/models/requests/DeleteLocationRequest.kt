package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteLocationRequest(
    val locationUID: String
)