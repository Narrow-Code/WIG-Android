package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val hash: String
)