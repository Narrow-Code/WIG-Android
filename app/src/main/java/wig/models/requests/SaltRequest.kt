package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SaltRequest(
    val username: String
)