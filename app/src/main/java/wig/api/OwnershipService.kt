package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.models.responses.CommonResponse
import wig.models.requests.OwnershipEditRequest
import wig.models.requests.OwnershipCreateRequest
import wig.models.responses.OwnershipResponse
import wig.models.responses.ownershipSearchResponse
import wig.models.requests.SearchRequest

interface OwnershipService {

    suspend fun ownershipSetLocation(ownershipUID: String, locationUID: String): CommonResponse

    suspend fun ownershipQuantity(changeType: String, amount: Int, ownershipUID: String): OwnershipResponse

    suspend fun ownershipCreate(newOwnershipRequest: OwnershipCreateRequest): OwnershipResponse

    suspend fun ownershipSearch(searchRequest: SearchRequest): ownershipSearchResponse

    suspend fun ownershipEdit(editOwnershipRequest: OwnershipEditRequest): CommonResponse


    companion object {
        fun create(): OwnershipService {
            return OwnershipServiceImpl(
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