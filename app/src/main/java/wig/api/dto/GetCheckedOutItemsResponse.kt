package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Borrower
import wig.models.Ownership

@Serializable
data class GetCheckedOutItemsResponse(
    val borrowers: List<Borrowers>,
    val message: String,
    val success: Boolean
)

@Serializable
data class Borrowers(
    val borrower: Borrower,
    val ownerships: MutableList<Ownership>
)