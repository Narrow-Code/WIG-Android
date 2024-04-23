package wig.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userUID: String,
    val username: String,
    val email: String,
    val emailConfirmed: String,
    val tier: String
)