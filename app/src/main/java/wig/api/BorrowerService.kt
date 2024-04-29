package wig.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import wig.models.responses.CheckoutResponse
import wig.models.requests.CheckoutRequest
import wig.models.responses.CreateBorrowerResponse
import wig.models.responses.GetBorrowersResponse
import wig.models.responses.GetCheckedOutItemsResponse

interface BorrowerService {

    suspend fun getBorrowers(): GetBorrowersResponse

    suspend fun createBorrower(name: String): CreateBorrowerResponse

    suspend fun checkout(borrowerUID: String, ownerships: CheckoutRequest): CheckoutResponse

    suspend fun checkIn(ownerships: CheckoutRequest): CheckoutResponse

    suspend fun getCheckedOutItems(): GetCheckedOutItemsResponse

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