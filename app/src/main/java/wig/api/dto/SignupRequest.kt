package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val username: String,
    val email: String,
    val hash: String,
    val salt: String
)