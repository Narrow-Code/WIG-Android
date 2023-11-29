package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ValidateResponse(
    val message: String,
    val success: Boolean
)