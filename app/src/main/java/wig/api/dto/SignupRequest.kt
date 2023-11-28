package wig.api.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the request to create a new user.
 *
 * @author Matthew McCaughey
 * @property username The username of the new user.
 * @property email The email address of the new user.
 * @property hash The hashed password of the new user.
 * @property salt The salt value used for hashing the password.
 */
@Serializable
data class SignupRequest(
    val username: String,
    val email: String,
    val hash: String,
    val salt: String
)