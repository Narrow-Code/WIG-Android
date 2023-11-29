package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val success: Boolean,
    val token: String,
    val uid: Int
)