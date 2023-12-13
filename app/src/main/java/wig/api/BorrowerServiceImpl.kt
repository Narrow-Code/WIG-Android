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
import wig.api.dto.CheckoutResponse
import wig.api.dto.CheckoutRequest
import wig.api.dto.CreateBorrowerResponse
import wig.api.dto.GetBorrowersResponse
import wig.models.Borrower
import wig.utils.JsonParse
import wig.utils.TokenManager

class BorrowerServiceImpl(private val client: HttpClient ) : BorrowerService {
    private val nullBorrowersList: List<Borrower> = listOf()
    private val nullIntList: List<Int> = listOf()
    private val nullBorrower = Borrower(0, "")

    override suspend fun getBorrowers(): GetBorrowersResponse {
        return try {
            client.get {
                url(HttpRoutes.GET_BORROWERS)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())

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
            client.post{
                url("${HttpRoutes.CREATE_BORROWER}?borrower=${name}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
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

    override suspend fun checkout(borrowerUID: Int, ownerships: CheckoutRequest): CheckoutResponse {
        return try {
            client.post{
                url("${HttpRoutes.CHECKOUT}?borrowerUID=${borrowerUID}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = ownerships
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullIntList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullIntList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullIntList)
        } catch(e: Exception) {
            CheckoutResponse(e.message.toString(), false, nullIntList)
        }
    }

    override suspend fun checkIn(ownerships: CheckoutRequest): CheckoutResponse {
        return try {
            client.post{
                url(HttpRoutes.CHECK_IN)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = ownerships
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullIntList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullIntList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullIntList)
        } catch(e: Exception) {
            CheckoutResponse(e.message.toString(), false, nullIntList)
        }
    }
}