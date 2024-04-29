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
import wig.models.responses.CommonResponse
import wig.models.responses.LocationResponse
import wig.models.responses.OwnershipResponse
import wig.models.responses.ScannerBarcodeResponse
import wig.models.entities.Borrower
import wig.models.entities.Item
import wig.models.entities.Location
import wig.models.entities.Ownership
import wig.models.entities.User
import wig.utils.JsonParse
import wig.managers.TokenManager

class ScannerServiceImpl(private val client: HttpClient ) : ScannerService {
    private val nullItem = Item("", "", "", "", "")
    private val nullBorrower = Borrower("", "")
    private val nullUser = User("", "", "", "", "")
    private val nullLocation = Location("", "", "", "", "", "", "", nullUser, null)
    private val nullOwnership = Ownership("", "", "", "", "", "", "", "", "", 0, "", "", nullUser, nullLocation, nullItem, nullBorrower)

    override suspend fun scannerBarcode(barcode: String): ScannerBarcodeResponse {
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
            ScannerBarcodeResponse(errorMessage, false, ArrayList())
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = when (e.response.status.value) {
                429 -> "429"
                else -> JsonParse().parseErrorMessage(e.response.receive<String>())
            }
            ScannerBarcodeResponse(errorMessage, false, ArrayList())
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            ScannerBarcodeResponse(errorMessage, false, ArrayList())
        } catch(e: Exception) {
            ScannerBarcodeResponse(e.message.toString(), false, ArrayList())
        }
    }

    override suspend fun scannerCheckQR(qr: String): CommonResponse {
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

    override suspend fun scannerQRLocation(qr: String): LocationResponse {
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

    override suspend fun scanQROwnership(qr: String): OwnershipResponse {
        return try {
            client.get {
                url("${HttpRoutes.SCAN_QR_OWNERSHIP}?qr=${qr}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            OwnershipResponse(errorMessage, false, nullOwnership)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            OwnershipResponse(errorMessage, false, nullOwnership)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            OwnershipResponse(errorMessage, false, nullOwnership)
        } catch(e: Exception) {
            OwnershipResponse(e.message.toString(), false, nullOwnership)
        }
    }

}