package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class BorrowerCreateRequest(
    val borrowerName: String
)