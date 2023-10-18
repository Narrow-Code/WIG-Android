package cloud.wig.android.api.users

import cloud.wig.android.api.users.dto.SaltRequest
import cloud.wig.android.api.users.dto.SaltResponse
import cloud.wig.android.api.users.dto.SignupRequest
import cloud.wig.android.api.users.dto.SignupResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Implementation of [UserService] interface for handling user-related operations.
 *
 * @author Matthew McCaughey
 * @property client An instance of [HttpClient] used for making HTTP requests.
 */
class UserServiceImpl(
    private val client: HttpClient
) : UserService {

    private val nullResponse: SignupResponse = SignupResponse("fail", false)

    // TODO remove when real API is implemented, maybe use to get user info
    override suspend fun getUser(): SignupResponse {
        return try {
            client.get<SignupResponse> {
                url(HttpRoutes.SIGNUP)}
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            return nullResponse
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            return nullResponse
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            return nullResponse
        } catch(e: Exception) {
            println("Error: ${e.message}")
            return nullResponse
        }
    }

    /**
     * Retrieves salt of a user with the provided [saltRequest].
     *
     * @param saltRequest Request object containing user UID for login.
     * @return [SaltResponse] containing the users specific salt value, or null if unsuccessful.
     */
    override suspend fun getSalt(saltRequest: SaltRequest): SaltResponse? {
        return try {
            client.post<SaltResponse> {
                url(HttpRoutes.LOGIN)
                contentType(ContentType.Application.Json)
                body = saltRequest
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch(e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    /**
     * Creates a new user with the provided [signupRequest].
     *
     * @param signupRequest Request object containing user information for signup.
     * @return [SignupResponse] containing the newly created user's information, or null if unsuccessful.
     */
    override suspend fun createUser(signupRequest: SignupRequest): SignupResponse? {
        return try {
            client.post<SignupResponse> {
                url(HttpRoutes.SIGNUP)
                contentType(ContentType.Application.Json)
                body = signupRequest
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            null
        } catch(e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }
}