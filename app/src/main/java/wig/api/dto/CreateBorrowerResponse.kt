package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Borrower

@Serializable
data class CreateBorrowerResponse(
    val message: String,
    val success: Boolean,
    val borrower: Borrower
)