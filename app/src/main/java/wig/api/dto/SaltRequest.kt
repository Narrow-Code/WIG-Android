package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class SaltRequest(
    val username: String
)