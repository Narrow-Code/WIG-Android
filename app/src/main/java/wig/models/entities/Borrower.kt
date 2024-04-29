package wig.models.entities

import kotlinx.serialization.Serializable

@Serializable
data class Borrower(
    val borrowerUID: String,
    val borrowerName: String
)