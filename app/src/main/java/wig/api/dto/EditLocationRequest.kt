package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class EditLocationRequest(
    val locationName: String,
    val locationDescription: String,
    val locationTags: String,
    val qr: String
)