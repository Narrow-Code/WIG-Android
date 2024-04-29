package wig.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val success: Boolean,
    val token: String,
    val uid: String
)