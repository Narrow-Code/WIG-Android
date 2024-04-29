package wig.models.responses

import kotlinx.serialization.Serializable
import wig.models.entities.Location
import wig.models.entities.Ownership

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