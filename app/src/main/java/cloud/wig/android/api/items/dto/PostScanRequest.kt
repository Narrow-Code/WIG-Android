package cloud.wig.android.api.items.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the request of scanning a barcode.
 */
@Serializable
data class PostScanRequest(
    val uid: String,
    val token: String
)