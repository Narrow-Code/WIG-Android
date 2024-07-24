package wig.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteBorrowerRequest(
    val borrowerUID: String
)