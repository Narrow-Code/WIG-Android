package wig.api.dto

import kotlinx.serialization.Serializable
import wig.models.Borrower
import wig.models.Location
import wig.models.Ownership

@Serializable
data class InventoryResponse(
    val message: String,
    val success: Boolean,
    val inventory: InventoryDTO
)

@Serializable
data class InventoryDTO(
    val parent: Location,
    val locations: MutableList<InventoryDTO>?,
    val ownerships: MutableList<Ownership>?
)