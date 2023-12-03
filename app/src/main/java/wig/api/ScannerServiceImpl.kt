package wig.api

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
import wig.api.dto.CommonResponse
import wig.api.dto.LocationResponse
import wig.api.dto.ScanResponse
import wig.models.Location
import wig.models.Ownership
import wig.models.User
import wig.utils.JsonParse
import wig.utils.TokenManager

class ScannerServiceImpl(private val client: HttpClient ) : ScannerService {
    private val nullUser = User(0, "", "", "", "")
    private val nullLocation = Location(0, 0, "", "", 0, "", "", "", nullUser, null)

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

    override suspend fun checkQR(qr: String): CommonResponse {
        return try {
            client.get {
                url("${HttpRoutes.CHECK_QR}?qr=${qr}")
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

    override suspend fun scanQRLocation(qr: String): LocationResponse {
        return try {
            client.get {
                url("${HttpRoutes.SCAN_QR_LOCATION}?qr=${qr}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LocationResponse(errorMessage, false, nullLocation)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LocationResponse(errorMessage, false, nullLocation)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LocationResponse(errorMessage, false, nullLocation)
        } catch(e: Exception) {
            LocationResponse(e.message.toString(), false, nullLocation)
        }
    }

}