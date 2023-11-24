package cloud.wig.android.api.items

import cloud.wig.android.api.items.dto.Ownership
import cloud.wig.android.api.items.dto.PostScanRequest
import cloud.wig.android.api.items.dto.PostScanResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType


/**
 * Implementation of [ItemService] interface for handling user-related operations.
 */
class ItemServiceImpl(private val client: HttpClient ) : ItemService {

    private val nullOwnerships: List<Ownership> = ArrayList()
    private val nullPostScanResponse: PostScanResponse = PostScanResponse("fail", "", nullOwnerships)

    override suspend fun postScan(postScanRequest: PostScanRequest, barcode: String): PostScanResponse {
        return try {
            client.post {
                url("${HttpRoutes.SCAN_ITEM}?barcode=${barcode}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                body = postScanRequest
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            nullPostScanResponse
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            nullPostScanResponse
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            nullPostScanResponse
        } catch(e: Exception) {
            println("Error: ${e.message}")
            nullPostScanResponse
        }
    }

}