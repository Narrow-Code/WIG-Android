package wig.api.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the response of scanning a barcode.
 */
@Serializable
data class PostScanResponse(
    val message: String,
    val success: Boolean,
    val ownership: List<Ownership>
)

@Serializable
data class Ownership(
    val ownershipUID: Int,
    val itemOwner: Int,
    val itemNumber: Int,
    val customItemName: String,
    val customItemImage: String,
    val customItemDescription: String,
    val itemLocation: Int,
    val itemQR: String,
    val itemTags: String,
    val itemQuantity: Int,
    val itemCheckedOut: String,
    val itemBorrower: Int,
    val user: User,
    val location: Location,
    val item: Item,
    val borrower: Borrower
)

@Serializable
data class User(
    val userUID: Int,
    val username: String,
    val email: String,
    val salt: String,
    val hash: String,
    val emailConfirmed: String,
    val tier: String
)

@Serializable
data class Location(
    val locationUID: Int,
    val locationOwner: Int,
    val locationName: String,
    val locationType: String,
    val locationParent: Int,
    val locationQR: String,
    val locationTags: String,
    val locationDescription: String,
    val user: User,
    val location: Location?
)

@Serializable
data class Item(
    val itemUID: Int,
    val barcode: String,
    val itemName: String,
    val itemBrand: String,
    val itemImage: String
)

@Serializable
data class Borrower(
    val borrowerUID: Int,
    val borrowerName: String
)