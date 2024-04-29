package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.models.responses.CommonResponse
import wig.models.responses.LocationResponse
import wig.models.responses.OwnershipResponse
import wig.models.responses.ScanResponse

interface ScannerService {

    suspend fun scan(barcode: String): ScanResponse

    suspend fun checkQR(qr: String): CommonResponse

    suspend fun scanQRLocation(qr: String): LocationResponse

    suspend fun scanQROwnership(qr: String): OwnershipResponse

    companion object {
        fun create(): ScannerService {
            return ScannerServiceImpl(
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