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
import wig.api.dto.EditLocationRequest
import wig.api.dto.InventoryDTO
import wig.api.dto.InventoryResponse
import wig.api.dto.LocationResponse
import wig.api.dto.SearchLocationResponse
import wig.api.dto.SearchRequest
import wig.models.Location
import wig.models.User
import wig.utils.JsonParse
import wig.utils.TokenManager

class LocationServiceImpl(private val client: HttpClient ) : LocationService {
    private val nullUser = User("", "", "", "", "")
    private val nullLocation = Location("", "", "", "", "", "", "", nullUser, null)
    private val nullInventoryDTO = InventoryDTO(nullLocation, ArrayList(), ArrayList())

    override suspend fun createLocation(name: String, locationQR: String): LocationResponse {
        return try {
            client.post {
                url("${HttpRoutes.CREATE_LOCATION}?location_name=${name}&location_qr=${locationQR}")
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

    override suspend fun unpackLocation(locationUID: String): InventoryResponse {
        return try {
            client.post {
                url("${HttpRoutes.UNPACK_LOCATION}?locationUID=${locationUID}")
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

    override suspend fun searchLocation(searchRequest: SearchRequest): SearchLocationResponse {
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
            SearchLocationResponse(errorMessage, false, ArrayList())
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            SearchLocationResponse(errorMessage, false, ArrayList())
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            SearchLocationResponse(errorMessage, false, ArrayList())
        } catch(e: Exception) {
            SearchLocationResponse(e.message.toString(), false, ArrayList())
        }
    }

    override suspend fun returnInventory(): InventoryResponse {
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

    override suspend fun locationEdit(editLocationRequest: EditLocationRequest, uid: String): CommonResponse {
        return try {
            client.put {
                url("${HttpRoutes.EDIT_LOCATION}?locationUID=${uid}")
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