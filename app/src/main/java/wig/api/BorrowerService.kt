package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.models.requests.BorrowerCreateRequest
import wig.models.responses.borrowerCheckedOutResponse
import wig.models.requests.CheckoutRequest
import wig.models.requests.DeleteBorrowerRequest
import wig.models.responses.borrowerCreateResponse
import wig.models.responses.borrowerGetAllResponse
import wig.models.responses.BorrowerGetInventoryResponse
import wig.models.responses.CommonResponse

interface BorrowerService {

    suspend fun borrowerGetAll(): borrowerGetAllResponse

    suspend fun borrowerCreate(borrowerCreateRequest: BorrowerCreateRequest): borrowerCreateResponse

    suspend fun borrowerCheckout(borrowerUID: String, ownerships: CheckoutRequest): borrowerCheckedOutResponse

    suspend fun borrowerCheckIn(ownerships: CheckoutRequest): borrowerCheckedOutResponse

    suspend fun borrowerGetInventory(): BorrowerGetInventoryResponse

    suspend fun borrowerDelete(deleteBorrowerRequest: DeleteBorrowerRequest): CommonResponse

    companion object {
        fun create(): BorrowerService {
            return BorrowerServiceImpl(
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
    } }