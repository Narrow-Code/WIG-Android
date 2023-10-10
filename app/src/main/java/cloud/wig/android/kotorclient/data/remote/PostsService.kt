package cloud.wig.android.kotorclient.data.remote

import cloud.wig.android.kotorclient.data.remote.dto.PostRequest
import cloud.wig.android.kotorclient.data.remote.dto.PostResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging

interface PostsService {

    suspend fun getPosts(): PostResponse

    suspend fun createPost(postRequest: PostRequest): PostResponse?

    companion object {
        fun create(): PostsService {
            return PostsServiceImpl(
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