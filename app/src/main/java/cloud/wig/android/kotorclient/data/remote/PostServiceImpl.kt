package cloud.wig.android.kotorclient.data.remote

import cloud.wig.android.kotorclient.data.remote.dto.PostRequest
import cloud.wig.android.kotorclient.data.remote.dto.PostResponse
import cloud.wig.android.kotorclient.data.remote.dto.UserData
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

    val nullResponse: PostResponse = PostResponse(UserData(0, "", "", "", "", "", ""), "fail", false)

    override suspend fun getPosts(): PostResponse {
        return try {
            client.get<PostResponse> {
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

    override suspend fun createPost(postRequest: PostRequest): PostResponse? {
        return try {
            client.post<PostResponse> {
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