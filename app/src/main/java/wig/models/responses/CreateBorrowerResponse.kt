package wig.models.responses

import kotlinx.serialization.Serializable
import wig.models.entities.Borrower

@Serializable
data class CreateBorrowerResponse(
    val message: String,
    val success: Boolean,
    val borrower: Borrower
)