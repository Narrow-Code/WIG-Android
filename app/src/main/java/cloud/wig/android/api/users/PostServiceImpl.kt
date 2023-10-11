package cloud.wig.android.api.users

import cloud.wig.android.api.users.dto.SignupRequest
import cloud.wig.android.api.users.dto.SignupResponse
import cloud.wig.android.api.users.dto.UserData
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PostsServiceImpl(
    private val client: HttpClient
) : PostsService {

    private val nullResponse: SignupResponse = SignupResponse(UserData(0, "", "", "", "", "", ""), "fail", false)

    override suspend fun getPosts(): SignupResponse {
        return try {
            client.get<SignupResponse> {
                url(HttpRoutes.POSTS)}
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

    override suspend fun createPost(postRequest: SignupRequest): SignupResponse? {
        return try {
            client.post<SignupResponse> {
                url(HttpRoutes.POSTS)
                contentType(ContentType.Application.Json)
                body = postRequest
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