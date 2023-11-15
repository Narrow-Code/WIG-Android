package cloud.wig.android.api.items

import cloud.wig.android.api.items.dto.PostScanRequest
import cloud.wig.android.api.items.dto.PostScanResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging


/**
 * Interface for interacting with item-related operations.
 *
 * @author Matthew McCaughey
 */
interface ItemService {

    suspend fun postScan(postScanRequest: PostScanRequest, barcode: String): PostScanResponse

    /**
     * Companion object for creating instances of [ItemService].
     */
    companion object {
        /**
         * Factory method for creating an instance of [ItemService].
         *
         * @return A new instance of [ItemServiceImpl].
         */
        fun create(): ItemService {
            return ItemServiceImpl(
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