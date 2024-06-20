package wig.models.entities

import kotlinx.serialization.Serializable

@Serializable
data class Ownership(
    val ownershipUID: String,
    val itemOwner: String,
    val itemNumber: String,
    var customItemName: String,
    val customItemImage: String,
    val customItemDescription: String,
    val itemLocation: String,
    val itemQR: String,
    val itemTags: String,
    var itemQuantity: Int,
    val itemCheckedOut: String,
    var itemBorrower: String,
    val user: User,
    var location: Location,
    val item: Item,
    var borrower: Borrower
)