package wig.models

import kotlinx.serialization.Serializable

@Serializable
data class Borrower(
    val borrowerUID: String,
    val borrowerName: String
)