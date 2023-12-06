package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.api.dto.CommonResponse
import wig.api.dto.LocationResponse
import wig.api.dto.ScanResponse

interface LocationService {

    suspend fun createLocation(type: String, name: String, locationQR: String): LocationResponse


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