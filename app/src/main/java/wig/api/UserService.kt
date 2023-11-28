package wig.api

import wig.api.dto.SaltRequest
import wig.api.dto.SaltResponse
import wig.api.dto.ValidateResponse
import wig.api.dto.LoginRequest
import wig.api.dto.LoginResponse
import wig.api.dto.SignupRequest
import wig.api.dto.SignupResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging


/**
 * Interface for interacting with user-related operations.
 *
 * @author Matthew McCaughey
 */
interface UserService {

    /**
     * Retrieves salt of a user with the provided [saltRequest].
     *
     * @param saltRequest Request object containing user UID for login.
     * @return [SaltResponse] containing the users specific salt value, or null if unsuccessful.
     */
    suspend fun getSalt(saltRequest: SaltRequest): SaltResponse

    /**
     * Logs in the user with the provided [loginRequest].
     *
     * @param loginRequest Request object containing username and hash.
     * @return [LoginResponse] containing the users specific UID and authentication token, or null if unsuccessful.
     */
    suspend fun login(loginRequest: LoginRequest): LoginResponse

    /**
     * @return [ValidateResponse] containing the users specific UID and authentication token, or null if unsuccessful.
     */
    suspend fun validate(): ValidateResponse


        /**
     * Creates a new user with the provided [signupRequest].
     *
     * @param signupRequest Request object containing user information for signup.
     * @return [SignupResponse] containing the newly created user's information, or null if unsuccessful.
     */
    suspend fun signup(signupRequest: SignupRequest): SignupResponse

    /**
     * Companion object for creating instances of [UserService].
     */
    companion object {
        /**
         * Factory method for creating an instance of [UserService].
         *
         * @return A new instance of [UserServiceImpl].
         */
        fun create(): UserService {
            return UserServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }


}