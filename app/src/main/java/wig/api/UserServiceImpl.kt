package wig.api

import wig.api.dto.SaltRequest
import wig.api.dto.SaltResponse
import wig.api.dto.CommonResponse
import wig.api.dto.LoginRequest
import wig.api.dto.LoginResponse
import wig.api.dto.SignupRequest
import wig.utils.TokenManager
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
import wig.utils.JsonParse

class UserServiceImpl(
    private val client: HttpClient ) : UserService {

    override suspend fun getSalt(saltRequest: SaltRequest): SaltResponse {
        return try {
            client.get {
                url("${HttpRoutes.SALT}?username=${saltRequest.username}")
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
            LoginResponse(errorMessage, false,"", 0)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LoginResponse(errorMessage, false,"", 0)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LoginResponse(errorMessage, false,"", 0)
        } catch(e: Exception) {
            LoginResponse(e.message.toString(), false,"", 0)
        }
    }

    override suspend fun validate(): CommonResponse {
        return try {
            client.post {
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
}