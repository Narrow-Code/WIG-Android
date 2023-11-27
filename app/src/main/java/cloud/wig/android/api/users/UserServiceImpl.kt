package cloud.wig.android.api.users

import android.util.Log
import cloud.wig.android.api.users.dto.GetSaltRequest
import cloud.wig.android.api.users.dto.GetSaltResponse
import cloud.wig.android.api.users.dto.PostLoginCheckResponse
import cloud.wig.android.api.users.dto.PostLoginRequest
import cloud.wig.android.api.users.dto.PostLoginResponse
import cloud.wig.android.api.users.dto.PostSignupRequest
import cloud.wig.android.api.users.dto.PostSignupResponse
import cloud.wig.android.datastore.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType


/**
 * Implementation of [UserService] interface for handling user-related operations.
 *
 * @property client An instance of [HttpClient] used for making HTTP requests.
 * @property nullPostSignupResponse a SignupResponse for failed process
 * @property nullGetSaltResponse a SaltResponse for failed process
 * @property nullPostLoginResponse a LoginResponse for failed process
 * @property nullPostLoginCheckResponse a LoginGetResponse for failed process
 */
class UserServiceImpl(
    private val client: HttpClient ) : UserService {

    private val nullPostSignupResponse: PostSignupResponse = PostSignupResponse("fail", false)
    private val nullGetSaltResponse: GetSaltResponse = GetSaltResponse("fail", false, "")
    private val nullPostLoginResponse: PostLoginResponse = PostLoginResponse("fail", false,"", 0)
    private val nullPostLoginCheckResponse: PostLoginCheckResponse = PostLoginCheckResponse("fail", false)

    /**
     * Retrieves salt of a user with the provided [getSaltRequest].
     *
     * @param getSaltRequest Request object containing user UID for login.
     * @return [GetSaltResponse] containing the users specific salt value, or null if unsuccessful.
     */
    override suspend fun getSalt(getSaltRequest: GetSaltRequest): GetSaltResponse {
        return try {
            client.get {
                url("${HttpRoutes.SALT}?username=${getSaltRequest.username}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            nullGetSaltResponse
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            nullGetSaltResponse
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            nullGetSaltResponse
        } catch(e: Exception) {
            println("Error: ${e.message}")
            nullGetSaltResponse
        }
    }

    /**
     * Logs in the user with the provided [postLoginRequest].
     *
     * @param postLoginRequest Request object containing username and hash.
     * @return [PostLoginResponse] containing the users specific UID and authentication token, or null if unsuccessful.
     */
    override suspend fun postLogin(postLoginRequest: PostLoginRequest): PostLoginResponse {
        return try {
            client.post {
                url(HttpRoutes.LOGIN)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                body = postLoginRequest
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            nullPostLoginResponse
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            nullPostLoginResponse
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            nullPostLoginResponse
        } catch(e: Exception) {
            println("Error: ${e.message}")
            nullPostLoginResponse
        }
    }

    /**
     * Authenticates user is still logged in at startup of app.
     *
     * @return [PostLoginCheckResponse] containing the users specific UID and authentication token, or null if unsuccessful.
     */
    override suspend fun postLoginCheck(): PostLoginCheckResponse {
        return try {
            client.post {
                url(HttpRoutes.LOGIN_CHECK)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            nullPostLoginCheckResponse
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            nullPostLoginCheckResponse
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            nullPostLoginCheckResponse
        } catch(e: Exception) {
            println("Error: ${e.message}")
            nullPostLoginCheckResponse
        }
    }

    /**
     * Creates a new user with the provided [postSignupRequest].
     *
     * @param postSignupRequest Request object containing user information for signup.
     * @return [PostSignupResponse] containing the newly created user's information, or null if unsuccessful.
     */
    override suspend fun postSignup(postSignupRequest: PostSignupRequest): PostSignupResponse {
        return try {
            client.post {
                url(HttpRoutes.SIGNUP)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                body = postSignupRequest
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            Log.d("createUser", "Error: ${e.response.status.description}")
            nullPostSignupResponse
        } catch(e: ClientRequestException) {
            // 4xx - responses
            Log.d("createUser", "Error: ${e.response.status.description}")
            nullPostSignupResponse
        } catch(e: ServerResponseException) {
            // 5xx - responses
            Log.d("createUser", "Error: ${e.response.status.description}")
            nullPostSignupResponse
        } catch(e: Exception) {
            Log.d("createUser", "Error: ${e.message}")
            nullPostSignupResponse
        }
    }
}