package wig.models

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val itemUID: Int,
    val barcode: String,
    val itemName: String,
    val itemBrand: String,
    val itemImage: String
)