package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the response to a user signup operation.
 *
 * @author Matthew McCaughey
 * @property message A message indicating the result of the signup operation.
 */
@Serializable
data class PostSignupResponse(
    val message: String
)