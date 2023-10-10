package cloud.wig.android.kotorclient.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val username: String,
    val email: String,
    val hash: String,
    val salt: ByteArray
)