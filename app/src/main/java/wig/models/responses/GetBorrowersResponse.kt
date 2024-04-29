package wig.models.responses

import kotlinx.serialization.Serializable
import wig.models.entities.Borrower

@Serializable
data class GetBorrowersResponse(
    val message: String,
    val success: Boolean,
    val borrowers: List<Borrower>
)