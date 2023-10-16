package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the response to a user signup operation.
 *
 * @author Matthew McCaughey
 * @property message A message indicating the result of the signup operation.
 * @property success A boolean indicating whether the signup operation was successful.
 * @property uid The users unique id
 * @property token The users authentication token
 */
@Serializable
data class SignupResponse(
    val message: String,
    val success: Boolean,
    val uid: Int,
    val token: String
)