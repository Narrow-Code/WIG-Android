package wig.api

import android.util.Log
import wig.api.dto.SaltRequest
import wig.api.dto.SaltResponse
import wig.api.dto.ValidateResponse
import wig.api.dto.LoginRequest
import wig.api.dto.LoginResponse
import wig.api.dto.SignupRequest
import wig.api.dto.SignupResponse
import wig.utils.TokenManager
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

    private val nullPostSignupResponse: SignupResponse = SignupResponse("fail", false)
    private val nullGetSaltResponse: SaltResponse = SaltResponse("fail", false, "")
    private val nullPostLoginResponse: LoginResponse = LoginResponse("fail", false,"", 0)
    private val nullPostLoginCheckResponse: ValidateResponse = ValidateResponse("fail", false)

    /**
     * Retrieves salt of a user with the provided [saltRequest].
     *
     * @param saltRequest Request object containing user UID for login.
     * @return [SaltResponse] containing the users specific salt value, or null if unsuccessful.
     */
    override suspend fun getSalt(saltRequest: SaltRequest): SaltResponse {
        return try {
            client.get {
                url("${HttpRoutes.SALT}?username=${saltRequest.username}")
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
     * Logs in the user with the provided [loginRequest].
     *
     * @param loginRequest Request object containing username and hash.
     * @return [LoginResponse] containing the users specific UID and authentication token, or null if unsuccessful.
     */
    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return try {
            client.post {
                url(HttpRoutes.LOGIN)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                body = loginRequest
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
     * @return [ValidateResponse] containing the users specific UID and authentication token, or null if unsuccessful.
     */
    override suspend fun validate(): ValidateResponse {
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
     * Creates a new user with the provided [signupRequest].
     *
     * @param signupRequest Request object containing user information for signup.
     * @return [SignupResponse] containing the newly created user's information, or null if unsuccessful.
     */
    override suspend fun signup(signupRequest: SignupRequest): SignupResponse {
        return try {
            client.post {
                url(HttpRoutes.SIGNUP)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                body = signupRequest
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