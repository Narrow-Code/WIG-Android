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
import wig.api.dto.CommonResponse
import wig.api.dto.LocationResponse
import wig.api.dto.UnpackResponse
import wig.models.Location
import wig.models.Ownership
import wig.models.User
import wig.utils.JsonParse
import wig.utils.TokenManager

class LocationServiceImpl(private val client: HttpClient ) : LocationService {
    private val nullUser = User(0, "", "", "", "")
    private val nullLocation = Location(0, 0, "", 0, "", "", "", nullUser, null)
    val nullLocationList: List<Location> = listOf()
    val nullOwnershipList: List<Ownership> = listOf()

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

    override suspend fun unpackLocation(locationUID: Int): UnpackResponse {
        return try {
            client.post {
                url("${HttpRoutes.CREATE_LOCATION}?location_uid=${locationUID}")
                contentType(ContentType.Application.Json)
                header("AppAuth", "what-i-got")
                header("Authorization", TokenManager.getToken())
            }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            UnpackResponse(errorMessage, false, nullOwnershipList, nullLocationList)
        } catch(e: ClientRequestException) {
            // 4xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            UnpackResponse(errorMessage, false, nullOwnershipList, nullLocationList)
        } catch(e: ServerResponseException) {
            // 5xx - responses
            val errorMessage = JsonParse().parseErrorMessage(e.response.receive<String>())
            UnpackResponse(errorMessage, false, nullOwnershipList, nullLocationList)
        } catch(e: Exception) {
            UnpackResponse(e.message.toString(), false, nullOwnershipList, nullLocationList)
        }
    }


}