package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String
)