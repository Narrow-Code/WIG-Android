package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    val name: String,
    val tags: String
)