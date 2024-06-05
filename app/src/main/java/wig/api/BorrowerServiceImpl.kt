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
import wig.models.responses.borrowerCheckedOutResponse
import wig.models.requests.CheckoutRequest
import wig.models.responses.borrowerCreateResponse
import wig.models.responses.borrowerGetAllResponse
import wig.models.responses.borrowerGetInventoryResponse
import wig.models.entities.Borrower
import wig.utils.JsonParse
import wig.managers.TokenManager
import wig.models.requests.BorrowerCreateRequest

class BorrowerServiceImpl(private val client: HttpClient ) : BorrowerService {
    private val nullBorrowerList: List<Borrower> = listOf()
    private val nullStringList: List<String> = listOf()
    private val nullBorrower = Borrower("", "")
    private val nullBorrowersList: List<Borrowers> = listOf()

    override suspend fun borrowerGetAll(): borrowerGetAllResponse {
        return try {
            client.get {
                url(HttpRoutes.BORROWER)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerGetAllResponse(errorMessage, false, nullBorrowerList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerGetAllResponse(errorMessage, false, nullBorrowerList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerGetAllResponse(errorMessage, false, nullBorrowerList)
        } catch(e: Exception) {
            borrowerGetAllResponse(e.message.toString(), false, nullBorrowerList)
        }
    }

    override suspend fun borrowerCreate(borrowerCreateRequest: BorrowerCreateRequest): borrowerCreateResponse {
        return try {
            client.post{
                url(HttpRoutes.BORROWER)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = borrowerCreateRequest
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerCreateResponse(errorMessage, false, nullBorrower)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerCreateResponse(errorMessage, false, nullBorrower)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerCreateResponse(errorMessage, false, nullBorrower)
        } catch(e: Exception) {
            borrowerCreateResponse(e.message.toString(), false, nullBorrower)
        }
    }

    override suspend fun borrowerCheckout(borrowerUID: String, ownerships: CheckoutRequest): borrowerCheckedOutResponse {
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
            borrowerCheckedOutResponse(errorMessage, false, nullStringList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerCheckedOutResponse(errorMessage, false, nullStringList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerCheckedOutResponse(errorMessage, false, nullStringList)
        } catch(e: Exception) {
            borrowerCheckedOutResponse(e.message.toString(), false, nullStringList)
        }
    }

    override suspend fun borrowerCheckIn(ownerships: CheckoutRequest): borrowerCheckedOutResponse {
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
            borrowerCheckedOutResponse(errorMessage, false, nullStringList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerCheckedOutResponse(errorMessage, false, nullStringList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerCheckedOutResponse(errorMessage, false, nullStringList)
        } catch(e: Exception) {
            borrowerCheckedOutResponse(e.message.toString(), false, nullStringList)
        }
    }

    override suspend fun borrowerGetInventory(): borrowerGetInventoryResponse {
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
            borrowerGetInventoryResponse(nullBorrowersList, errorMessage, false)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerGetInventoryResponse(nullBorrowersList, errorMessage, false)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            borrowerGetInventoryResponse(nullBorrowersList, errorMessage, false)
        } catch(e: Exception) {
            borrowerGetInventoryResponse(nullBorrowersList, e.message.toString(), false)
        }
    }

}