package wig.models

import kotlinx.serialization.Serializable

@Serializable
data class Borrower(
    val borrowerUID: Int,
    val borrowerName: String
)