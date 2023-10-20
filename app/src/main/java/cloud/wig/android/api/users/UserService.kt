package cloud.wig.android.api.users

import cloud.wig.android.api.users.dto.LoginGetRequest
import cloud.wig.android.api.users.dto.LoginGetResponse
import cloud.wig.android.api.users.dto.LoginRequest
import cloud.wig.android.api.users.dto.LoginResponse
import cloud.wig.android.api.users.dto.SaltRequest
import cloud.wig.android.api.users.dto.SaltResponse
import cloud.wig.android.api.users.dto.SignupRequest
import cloud.wig.android.api.users.dto.SignupResponse
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
    suspend fun loginUser(loginRequest: LoginRequest): LoginResponse

    /**
     * Authenticates user is still logged in at startup of app [loginGetRequest].
     *
     * @param loginGetRequest Request object containing username and hash.
     * @return [LoginGetResponse] containing the users specific UID and authentication token, or null if unsuccessful.
     */
    suspend fun getLoginUser(loginGetRequest: LoginGetRequest): LoginGetResponse


        /**
     * Creates a new user with the provided [signupRequest].
     *
     * @param signupRequest Request object containing user information for signup.
     * @return [SignupResponse] containing the newly created user's information, or null if unsuccessful.
     */
    suspend fun createUser(signupRequest: SignupRequest): SignupResponse

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