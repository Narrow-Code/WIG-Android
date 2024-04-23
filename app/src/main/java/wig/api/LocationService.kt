package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.api.dto.CommonResponse
import wig.api.dto.EditLocationRequest
import wig.api.dto.InventoryResponse
import wig.api.dto.LocationResponse
import wig.api.dto.ScanResponse
import wig.api.dto.SearchLocationResponse
import wig.api.dto.SearchOwnershipResponse
import wig.api.dto.SearchRequest
import wig.api.dto.UnpackResponse

interface LocationService {

    suspend fun createLocation(name: String, locationQR: String): LocationResponse

    suspend fun unpackLocation(locationUID: String): UnpackResponse

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