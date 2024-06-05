package wig.models.responses

import kotlinx.serialization.Serializable
import wig.models.entities.Borrower
import wig.models.entities.Ownership

@Serializable
data class borrowerGetInventoryResponse(
    val borrowers: List<Borrowers>,
    val message: String,
    val success: Boolean
)

@Serializable
data class Borrowers(
    val borrower: Borrower,
    val ownerships: MutableList<Ownership>
)