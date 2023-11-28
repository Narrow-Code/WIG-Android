package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.api.dto.ScanResponse


/**
 * Interface for interacting with item-related operations.
 *
 * @author Matthew McCaughey
 */
interface ScannerService {

    suspend fun scan(barcode: String): ScanResponse

    /**
     * Companion object for creating instances of [ScannerService].
     */
    companion object {
        /**
         * Factory method for creating an instance of [ScannerService].
         *
         * @return A new instance of [ScannerServiceImpl].
         */
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