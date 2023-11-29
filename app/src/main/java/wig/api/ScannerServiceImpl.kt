package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import wig.api.dto.ScanResponse
import wig.utils.JsonParse
import wig.utils.TokenManager

class ScannerServiceImpl(private val client: HttpClient ) : ScannerService {

    override suspend fun scan(barcode: String): ScanResponse {
        return try {
            client.post {
                url("${HttpRoutes.SCAN_BARCODE}?barcode=${barcode}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            ScanResponse(errorMessage, false, ArrayList())
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            ScanResponse(errorMessage, false, ArrayList())
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            ScanResponse(errorMessage, false, ArrayList())
        } catch(e: Exception) {
            ScanResponse(e.message.toString(), false, ArrayList())
        }
    }
}