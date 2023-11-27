package cloud.wig.android.api.items.dto

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
    val ownership_uid: Int,
    val item_owner: Int,
    val item_number: Int,
    val custom_item_name: String,
    val custom_item_img: String,
    val custom_item_description: String,
    val item_location: Int,
    val item_qr: String,
    val item_tags: String,
    val item_quantity: Int,
    val item_checked_out: String,
    val item_borrower: Int,
    val User: User,
    val Location: Location,
    val Item: Item,
    val Borrower: Borrower
)

@Serializable
data class User(
    val user_uid: Int,
    val username: String,
    val email: String,
    val salt: String,
    val hash: String,
    val email_confirmed: String,
    val tier_level: String
)

@Serializable
data class Location(
    val location_uid: Int,
    val location_owner: Int,
    val location_name: String,
    val location_type: String,
    val location_location: Int,
    val location_qr: String,
    val location_tags: String,
    val location_description: String,
    val User: User,
    val Location: Location?
)

@Serializable
data class Item(
    val item_uid: Int,
    val barcode: String,
    val item_name: String,
    val item_brand: String,
    val item_img: String
)

@Serializable
data class Borrower(
    val borrower_uid: Int,
    val borrower_name: String
)