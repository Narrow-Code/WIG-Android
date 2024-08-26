package wig.api

import wig.models.requests.SaltRequest
import wig.models.responses.SaltResponse
import wig.models.responses.CommonResponse
import wig.models.requests.LoginRequest
import wig.models.responses.LoginResponse
import wig.models.requests.SignupRequest
import wig.managers.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import wig.models.requests.EmailRequest
import wig.utils.JsonParse

class UserServiceImpl(
    private val client: HttpClient ) : UserService {

    override suspend fun getSalt(saltRequest: SaltRequest): SaltResponse {
        return try {
            client.get {
                url("${HttpRoutes.BASE_URL}/user/${saltRequest.username}/salt")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            SaltResponse(errorMessage, false, "")
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            SaltResponse(errorMessage, false, "")
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            SaltResponse(errorMessage, false, "")
        } catch(e: Exception) {
            SaltResponse(e.message.toString(), false, "")
        }
    }

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
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LoginResponse(errorMessage, false,"", "")
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LoginResponse(errorMessage, false,"", "")
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LoginResponse(errorMessage, false,"", "")
        } catch(e: Exception) {
            LoginResponse(e.message.toString(), false,"", "")
        }
    }

    override suspend fun validate(): CommonResponse {
        return try {
            client.get {
                url(HttpRoutes.LOGIN_CHECK)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch(e: Exception) {
            CommonResponse(e.message.toString(), false)
        }
    }

    override suspend fun signup(signupRequest: SignupRequest): CommonResponse {
        return try {
            client.post {
                url(HttpRoutes.SIGNUP)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                body = signupRequest
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch(e: Exception) {
            CommonResponse(e.message.toString(), false)
        }
    }

    override suspend fun ping(hostname: String, port: String): CommonResponse {
        return try {
            client.get {
                url("$hostname:$port/ping")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch(e: Exception) {
            CommonResponse(e.message.toString(), false)
        }
    }

    override suspend fun resendVerification(emailRequest: EmailRequest): CommonResponse {
        return try {
            client.post {
                url(HttpRoutes.RESEND_EMAIL)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                body = emailRequest
            }
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch (e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch (e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch (e: Exception) {
            CommonResponse(e.message.toString(), false)
        }
    }

    override suspend fun forgotPassword(emailRequest: EmailRequest): CommonResponse {
        return try {
            client.post {
                url(HttpRoutes.FORGOT_PASSWORD)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                body = emailRequest
            }
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch (e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch (e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CommonResponse(errorMessage, false)
        } catch (e: Exception) {
            CommonResponse(e.message.toString(), false)
        }
    }
}