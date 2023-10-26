package cloud.wig.android.api.users

import cloud.wig.android.api.users.dto.GetSaltRequest
import cloud.wig.android.api.users.dto.GetSaltResponse
import cloud.wig.android.api.users.dto.PostLoginCheckRequest
import cloud.wig.android.api.users.dto.PostLoginCheckResponse
import cloud.wig.android.api.users.dto.PostLoginRequest
import cloud.wig.android.api.users.dto.PostLoginResponse
import cloud.wig.android.api.users.dto.PostSignupRequest
import cloud.wig.android.api.users.dto.PostSignupResponse
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
     * Retrieves salt of a user with the provided [getSaltRequest].
     *
     * @param getSaltRequest Request object containing user UID for login.
     * @return [GetSaltResponse] containing the users specific salt value, or null if unsuccessful.
     */
    suspend fun getSalt(getSaltRequest: GetSaltRequest): GetSaltResponse

    /**
     * Logs in the user with the provided [postLoginRequest].
     *
     * @param postLoginRequest Request object containing username and hash.
     * @return [PostLoginResponse] containing the users specific UID and authentication token, or null if unsuccessful.
     */
    suspend fun postLogin(postLoginRequest: PostLoginRequest): PostLoginResponse

    /**
     * Authenticates user is still logged in at startup of app [postLoginCheckRequest].
     *
     * @param postLoginCheckRequest Request object containing username and hash.
     * @return [PostLoginCheckResponse] containing the users specific UID and authentication token, or null if unsuccessful.
     */
    suspend fun postLoginCheck(postLoginCheckRequest: PostLoginCheckRequest): PostLoginCheckResponse


        /**
     * Creates a new user with the provided [postSignupRequest].
     *
     * @param postSignupRequest Request object containing user information for signup.
     * @return [PostSignupResponse] containing the newly created user's information, or null if unsuccessful.
     */
    suspend fun postSignup(postSignupRequest: PostSignupRequest): PostSignupResponse

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