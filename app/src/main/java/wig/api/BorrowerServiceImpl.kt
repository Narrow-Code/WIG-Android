package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import wig.api.dto.CommonResponse
import wig.api.dto.GetBorrowersResponse
import wig.api.dto.LocationResponse
import wig.api.dto.SaltRequest
import wig.api.dto.SaltResponse
import wig.api.dto.UnpackResponse
import wig.models.Location
import wig.models.Ownership
import wig.models.User
import wig.utils.JsonParse
import wig.utils.TokenManager

class BorrowerServiceImpl(private val client: HttpClient ) : BorrowerService {
    private val nullBorrowersList: List<Int> = listOf()

    override suspend fun getBorrowers(): GetBorrowersResponse {
        return try {
            client.get {
                url(HttpRoutes.GET_BORROWERS)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            GetBorrowersResponse(errorMessage, false, nullBorrowersList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            GetBorrowersResponse(errorMessage, false, nullBorrowersList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            GetBorrowersResponse(errorMessage, false, nullBorrowersList)
        } catch(e: Exception) {
            GetBorrowersResponse(e.message.toString(), false, nullBorrowersList)
        }
    }
}