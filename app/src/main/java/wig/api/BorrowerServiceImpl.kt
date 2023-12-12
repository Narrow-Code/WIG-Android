package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import wig.api.dto.CreateBorrowerResponse
import wig.api.dto.GetBorrowersResponse
import wig.models.Borrower
import wig.utils.JsonParse

class BorrowerServiceImpl(private val client: HttpClient ) : BorrowerService {
    private val nullBorrowersList: List<Int> = listOf()
    private val nullBorrower = Borrower(0, "")

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

    override suspend fun createBorrower(name: String): CreateBorrowerResponse {
        return try {
            client.get {
                url("${HttpRoutes.GET_BORROWERS}?borrower=${name}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CreateBorrowerResponse(errorMessage, false, nullBorrower)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CreateBorrowerResponse(errorMessage, false, nullBorrower)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CreateBorrowerResponse(errorMessage, false, nullBorrower)
        } catch(e: Exception) {
            CreateBorrowerResponse(e.message.toString(), false, nullBorrower)
        }
    }
}