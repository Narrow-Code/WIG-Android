package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.api.dto.CommonResponse
import wig.api.dto.EditOwnershipRequest
import wig.api.dto.LocationResponse
import wig.api.dto.NewOwnershipRequest
import wig.api.dto.OwnershipResponse
import wig.api.dto.ScanResponse
import wig.api.dto.SearchOwnershipResponse
import wig.api.dto.SearchRequest

interface OwnershipService {

    suspend fun setLocation(ownershipUID: String, locationQR: String): CommonResponse

    suspend fun changeQuantity(changeType: String, amount: Int, ownershipUID: String): OwnershipResponse

    suspend fun createOwnershipNoItem(newOwnershipRequest: NewOwnershipRequest): OwnershipResponse

    suspend fun searchOwnership(searchRequest: SearchRequest): SearchOwnershipResponse

    suspend fun editOwnership(editOwnershipRequest: EditOwnershipRequest, uid: String): CommonResponse


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