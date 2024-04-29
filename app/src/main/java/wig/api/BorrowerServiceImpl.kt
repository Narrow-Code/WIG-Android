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
import wig.models.responses.Borrowers
import wig.models.responses.CheckoutResponse
import wig.models.requests.CheckoutRequest
import wig.models.responses.CreateBorrowerResponse
import wig.models.responses.GetBorrowersResponse
import wig.models.responses.GetCheckedOutItemsResponse
import wig.models.entities.Borrower
import wig.utils.JsonParse
import wig.utils.TokenManager

class BorrowerServiceImpl(private val client: HttpClient ) : BorrowerService {
    private val nullBorrowerList: List<Borrower> = listOf()
    private val nullStringList: List<String> = listOf()
    private val nullBorrower = Borrower("", "")
    private val nullBorrowersList: List<Borrowers> = listOf()

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
            GetBorrowersResponse(errorMessage, false, nullBorrowerList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            GetBorrowersResponse(errorMessage, false, nullBorrowerList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            GetBorrowersResponse(errorMessage, false, nullBorrowerList)
        } catch(e: Exception) {
            GetBorrowersResponse(e.message.toString(), false, nullBorrowerList)
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

    override suspend fun checkout(borrowerUID: String, ownerships: CheckoutRequest): CheckoutResponse {
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
            CheckoutResponse(errorMessage, false, nullStringList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullStringList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullStringList)
        } catch(e: Exception) {
            CheckoutResponse(e.message.toString(), false, nullStringList)
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
            CheckoutResponse(errorMessage, false, nullStringList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullStringList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            CheckoutResponse(errorMessage, false, nullStringList)
        } catch(e: Exception) {
            CheckoutResponse(e.message.toString(), false, nullStringList)
        }
    }

    override suspend fun getCheckedOutItems(): GetCheckedOutItemsResponse {
        return try {
            client.get {
                url(HttpRoutes.GET_CHECKED_OUT)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            GetCheckedOutItemsResponse(nullBorrowersList, errorMessage, false)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            GetCheckedOutItemsResponse(nullBorrowersList, errorMessage, false)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            GetCheckedOutItemsResponse(nullBorrowersList, errorMessage, false)
        } catch(e: Exception) {
            GetCheckedOutItemsResponse(nullBorrowersList, e.message.toString(), false)
        }
    }

}