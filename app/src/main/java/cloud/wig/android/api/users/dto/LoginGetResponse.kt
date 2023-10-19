package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the response to the lofin verification.
 *
 * @author Matthew McCaughey
 * @property message A message indicating the result of the login verification.
 * @property success A boolean indicating whether the login verification was successful.
 */
@Serializable
data class LoginGetResponse(
    val message: String,
    val success: Boolean
)