package wig.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetBorrowersResponse(
    val message: String,
    val success: Boolean,
    val borrowers: List<Int>
)