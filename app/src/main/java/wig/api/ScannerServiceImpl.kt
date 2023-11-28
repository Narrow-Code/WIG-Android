package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import wig.api.dto.Ownership
import wig.api.dto.ScanResponse
import wig.utils.TokenManager


/**
 * Implementation of [ScannerService] interface for handling user-related operations.
 */
class ScannerServiceImpl(private val client: HttpClient ) : ScannerService {

    private val nullOwnerships: List<Ownership> = ArrayList()
    private val nullPostScanResponse: ScanResponse = ScanResponse("fail", false, nullOwnerships)

    override suspend fun scan(barcode: String): ScanResponse {
        return try {
            client.post {
                url("${HttpRoutes.SCAN_ITEM}?barcode=${barcode}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
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