package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the response to a user signup operation.
 *
 * @author Matthew McCaughey
 * @property data The user data associated with the response.
 * @property message A message indicating the result of the signup operation.
 * @property success A boolean indicating whether the signup operation was successful.
 */
@Serializable
data class SignupResponse(
    val data: UserData,
    val message: String,
    val success: Boolean
)

/**
 * Represents a data class for user-specific data.
 *
 * @property uid The user ID.
 * @property username The username of the user.
 * @property email The email address of the user.
 * @property salt The salt value associated with the user.
 * @property hash The hashed password of the user.
 * @property email_confirmed The confirmation status of the user's email address.
 * @property tier_level The user's tier level.
 */
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