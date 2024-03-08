package wig.models

import kotlinx.serialization.Serializable

@Serializable
data class Ownership(
    val ownershipUID: Int,
    val itemOwner: Int,
    val itemNumber: Int,
    var customItemName: String,
    val customItemImage: String,
    val customItemDescription: String,
    val itemLocation: Int,
    val itemQR: String,
    val itemTags: String,
    var itemQuantity: Int,
    val itemCheckedOut: String,
    val itemBorrower: Int,
    val user: User,
    var location: Location,
    val item: Item,
    val borrower: Borrower
)