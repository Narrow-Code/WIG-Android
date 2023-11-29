package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    val message: String,
    val success: Boolean
)