package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse(
    val message: String,
    val success: Boolean
)