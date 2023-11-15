package cloud.wig.android.api.items.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the response of scanning a barcode.
 */
@Serializable
data class PostScanResponse(
    val message: String,
    val success: Boolean,
    val barcode: String,
    val brand: String,
    val image: String,
    val owner: Int,
    val item: String,
    val ownership: List<Ownership>
)

@Serializable
data class Ownership(
    val ownership_uid: Int,
    val custom_item_name: String,
    val custom_item_img: String,
    val custom_item_description: String,
    val item_location: String,
    val item_qr: String,
    val item_tags: String,
    val item_quantity: Int,
    val item_checked_out: String,
    val item_borrower: Int
)