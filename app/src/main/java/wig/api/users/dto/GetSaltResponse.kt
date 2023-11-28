package wig.api.users.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data class for the response to the first step of login, retrieving salt value.
 *
 * @author Matthew McCaughey
 * @property message A message indicating the result of the signup operation.
 * @property salt The salt value
 */
@Serializable
data class GetSaltResponse(
    val message: String,
    val success: Boolean,
    val salt: String
)