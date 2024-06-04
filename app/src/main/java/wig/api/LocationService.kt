package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.models.requests.LocationCreateRequest
import wig.models.responses.CommonResponse
import wig.models.requests.LocationEditRequest
import wig.models.responses.InventoryResponse
import wig.models.responses.LocationResponse
import wig.models.responses.LocationSearchResponse
import wig.models.requests.SearchRequest

interface LocationService {

    suspend fun locationCreate(locationCreateRequest: LocationCreateRequest): LocationResponse

    suspend fun locationUnpack(locationUID: String): InventoryResponse

    suspend fun locationSearch(searchRequest: SearchRequest): LocationSearchResponse

    suspend fun locationGetInventory(): InventoryResponse

    suspend fun locationEdit(editLocationRequest: LocationEditRequest, uid: String): CommonResponse


    companion object {
        fun create(): LocationService {
            return LocationServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }


}