package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the request of login step one, salt value return.
 *
 * @author Matthew McCaughey
 * @property username The username of the user.
 */
@Serializable
data class SaltRequest(
    val username: String
)