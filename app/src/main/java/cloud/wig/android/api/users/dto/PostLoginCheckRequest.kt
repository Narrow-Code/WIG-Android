package cloud.wig.android.api.users.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the request of login verification.
 *
 * @author Matthew McCaughey
 * @property uid The users UID
 * @property token THe users authentication token
 */
@Serializable
data class PostLoginCheckRequest(
    val uid: String,
    val token: String
)