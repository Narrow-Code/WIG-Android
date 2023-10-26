package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the request of login step two, password verification.
 *
 * @author Matthew McCaughey
 * @property username The username of the user.
 * @property hash The hashed password of the user.
 */
@Serializable
data class PostLoginRequest(
    val username: String,
    val hash: String
)