package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import wig.models.responses.CommonResponse
import wig.models.requests.OwnershipEditRequest
import wig.models.requests.OwnershipCreateRequest
import wig.models.responses.OwnershipResponse
import wig.models.responses.ownershipSearchResponse
import wig.models.requests.SearchRequest
import wig.models.entities.Borrower
import wig.models.entities.Item
import wig.models.entities.Location
import wig.models.entities.Ownership
import wig.models.entities.User
import wig.utils.JsonParse
import wig.managers.TokenManager
import wig.models.requests.DeleteOwnershipRequest
import wig.models.requests.SetLocationRequest

class OwnershipServiceImpl(private val client: HttpClient ) : OwnershipService {
    private val nullUser = User("", "", "", "", "")
    private val nullItem = Item("", "", "", "", "")
    private val nullBorrower = Borrower("", "")
    private val nullLocation = Location("", "", "", "", "", "", "", nullUser, null)
    private val nullOwnership = Ownership("", "", "", "", "", "", "", "", "", 0, "", "", nullUser, nullLocation, nullItem, nullBorrower)

    override suspend fun ownershipSetLocation(ownershipUID: String, locationUID: String): CommonResponse {
        return try {
            val setLocationRequest = SetLocationRequest(locationUID)
            client.put {
                url("${HttpRoutes.OWNERSHIP}/${ownershipUID}/set-parent")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = setLocationRequest
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

    override suspend fun ownershipQuantity(changeType: String, amount: Int, ownershipUID: String): OwnershipResponse {
        return try {
            client.put {
                url("${HttpRoutes.OWNERSHIP}/${ownershipUID}/quantity/${changeType}?amount=${amount}")
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

    override suspend fun ownershipCreate(newOwnershipRequest: OwnershipCreateRequest): OwnershipResponse {
        return try {
            client.post {
                url(HttpRoutes.OWNERSHIP)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = newOwnershipRequest
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

    override suspend fun ownershipSearch(searchRequest: SearchRequest): ownershipSearchResponse {
        return try {
            client.post {
                url(HttpRoutes.SEARCH_OWNERSHIP)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = searchRequest
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            ownershipSearchResponse(errorMessage, false, ArrayList())
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            ownershipSearchResponse(errorMessage, false, ArrayList())
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            ownershipSearchResponse(errorMessage, false, ArrayList())
        } catch(e: Exception) {
            ownershipSearchResponse(e.message.toString(), false, ArrayList())
        }
    }

    override suspend fun ownershipEdit(editOwnershipRequest: OwnershipEditRequest): CommonResponse {
        return try {
            client.put {
                url(HttpRoutes.OWNERSHIP)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = editOwnershipRequest
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

    override suspend fun ownershipDelete(ownershipUID: String): CommonResponse {
        return try {
            val deleteOwnershipRequest = DeleteOwnershipRequest(ownershipUID)
            client.delete {
                url(HttpRoutes.OWNERSHIP)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = deleteOwnershipRequest
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

}