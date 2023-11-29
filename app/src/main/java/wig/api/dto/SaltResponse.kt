package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class SaltResponse(
    val message: String,
    val success: Boolean,
    val salt: String
)