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
import wig.models.responses.CommonResponse
import wig.models.requests.LocationEditRequest
import wig.models.responses.InventoryDTO
import wig.models.responses.InventoryResponse
import wig.models.responses.LocationResponse
import wig.models.responses.LocationSearchResponse
import wig.models.requests.SearchRequest
import wig.models.entities.Location
import wig.models.entities.User
import wig.utils.JsonParse
import wig.managers.TokenManager
import wig.models.requests.LocationCreateRequest

class LocationServiceImpl(private val client: HttpClient ) : LocationService {
    private val nullUser = User("", "", "", "", "")
    private val nullLocation = Location("", "", "", "", "", "", "", nullUser, null)
    private val nullInventoryDTO = InventoryDTO(nullLocation, ArrayList(), ArrayList())

    override suspend fun locationCreate(locationCreateRequest: LocationCreateRequest): LocationResponse {
        return try {
            client.post {
                url(HttpRoutes.LOCATION)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = locationCreateRequest
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

    override suspend fun locationUnpack(locationUID: String): InventoryResponse {
        return try {
            client.get {
                url("${HttpRoutes.LOCATION}/${locationUID}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            InventoryResponse(errorMessage, false, nullInventoryDTO)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            InventoryResponse(errorMessage, false, nullInventoryDTO)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            InventoryResponse(errorMessage, false, nullInventoryDTO)
        } catch(e: Exception) {
            InventoryResponse(e.message.toString(), false, nullInventoryDTO)
        }
    }

    override suspend fun locationSearch(searchRequest: SearchRequest): LocationSearchResponse {
        return try {
            client.post {
                url(HttpRoutes.SEARCH_LOCATION)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = searchRequest
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LocationSearchResponse(errorMessage, false, ArrayList())
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LocationSearchResponse(errorMessage, false, ArrayList())
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            LocationSearchResponse(errorMessage, false, ArrayList())
        } catch(e: Exception) {
            LocationSearchResponse(e.message.toString(), false, ArrayList())
        }
    }

    override suspend fun locationGetInventory(): InventoryResponse {
        return try {
            client.get {
                url(HttpRoutes.RETURN_INVENTORY)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            InventoryResponse(errorMessage, false, nullInventoryDTO)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            InventoryResponse(errorMessage, false, nullInventoryDTO)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            InventoryResponse(errorMessage, false, nullInventoryDTO)
        } catch(e: Exception) {
            InventoryResponse(e.message.toString(), false, nullInventoryDTO)
        }
    }

    override suspend fun locationEdit(editLocationRequest: LocationEditRequest): CommonResponse {
        return try {
            client.put {
                url(HttpRoutes.LOCATION)
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
                body = editLocationRequest
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