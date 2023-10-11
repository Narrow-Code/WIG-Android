package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    val data: UserData,
    val message: String,
    val success: Boolean
)

@Serializable
data class UserData(
    val uid: Int,
    val username: String,
    val email: String,
    val salt: String,
    val hash: String,
    val email_confirmed: String,
    val tier_level: String
)