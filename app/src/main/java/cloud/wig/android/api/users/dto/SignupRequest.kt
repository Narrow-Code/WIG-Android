package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val username: String,
    val email: String,
    val hash: String,
    val salt: ByteArray
)