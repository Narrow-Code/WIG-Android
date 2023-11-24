package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the response to the second step of login, password verification.
 *
 * @author Matthew McCaughey
 * @property message A message indicating the result of the login operation.
 * @property token The authentication token of the user.
 * @property uid The users UID
 */
@Serializable
data class PostLoginResponse(
    val message: String,
    val token: String,
    val uid: Int
)