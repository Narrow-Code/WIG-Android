package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.models.responses.CommonResponse
import wig.models.requests.EditLocationRequest
import wig.models.responses.InventoryResponse
import wig.models.responses.LocationResponse
import wig.models.responses.SearchLocationResponse
import wig.models.requests.SearchRequest

interface LocationService {

    suspend fun createLocation(name: String, locationQR: String): LocationResponse

    suspend fun unpackLocation(locationUID: String): InventoryResponse

    suspend fun searchLocation(searchRequest: SearchRequest): SearchLocationResponse

    suspend fun returnInventory(): InventoryResponse

    suspend fun locationEdit(editLocationRequest: EditLocationRequest, uid: String): CommonResponse


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