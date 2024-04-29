package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import wig.models.responses.CommonResponse
import wig.models.requests.EditOwnershipRequest
import wig.models.requests.NewOwnershipRequest
import wig.models.responses.OwnershipResponse
import wig.models.responses.SearchOwnershipResponse
import wig.models.requests.SearchRequest
import wig.models.entities.Borrower
import wig.models.entities.Item
import wig.models.entities.Location
import wig.models.entities.Ownership
import wig.models.entities.User
import wig.utils.JsonParse
import wig.managers.TokenManager

class OwnershipServiceImpl(private val client: HttpClient ) : OwnershipService {
    private val nullUser = User("", "", "", "", "")
    private val nullItem = Item("", "", "", "", "")
    private val nullBorrower = Borrower("", "")
    private val nullLocation = Location("", "", "", "", "", "", "", nullUser, null)
    private val nullOwnership = Ownership("", "", "", "", "", "", "", "", "", 0, "", "", nullUser, nullLocation, nullItem, nullBorrower)

    override suspend fun setLocation(ownershipUID: String, locationQR: String): CommonResponse {
        return try {
            client.put {
                url("${HttpRoutes.SET_OWNERSHIP_LOCATION}?ownershipUID=${ownershipUID}&location_qr=${locationQR}")
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

    override suspend fun changeQuantity(changeType: String, amount: Int, ownershipUID: String): OwnershipResponse {
        return try {
            client.put {
                url("${HttpRoutes.CHANGE_OWNERSHIP_QUANTITY}${changeType}?ownershipUID=${ownershipUID}&amount=${amount}")
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

    override suspend fun createOwnershipNoItem(newOwnershipRequest: NewOwnershipRequest): OwnershipResponse {
        return try {
            client.post {
                url("${HttpRoutes.CREATE_OWNERSHIP}?item_uid=1")
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

    override suspend fun searchOwnership(searchRequest: SearchRequest): SearchOwnershipResponse {
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
            SearchOwnershipResponse(errorMessage, false, ArrayList())
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            SearchOwnershipResponse(errorMessage, false, ArrayList())
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            SearchOwnershipResponse(errorMessage, false, ArrayList())
        } catch(e: Exception) {
            SearchOwnershipResponse(e.message.toString(), false, ArrayList())
        }
    }

    override suspend fun editOwnership(editOwnershipRequest: EditOwnershipRequest, uid: String): CommonResponse {
        return try {
            client.put {
                url("${HttpRoutes.EDIT_OWNERSHIP}?ownershipUID=${uid}")
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


}