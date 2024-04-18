package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Borrower
import wig.models.Location
import wig.models.Ownership

@Serializable
data class InventoryResponse(
    val message: String,
    val success: Boolean,
    val inventory: List<InventoryDTO>
)

@Serializable
data class InventoryDTO(
    val parent: Location,
    val locations: List<InventoryDTO>,
    val ownerships: List<Ownership>
)