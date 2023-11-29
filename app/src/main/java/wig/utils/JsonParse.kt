package wig.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class JsonParse {
    fun parseErrorMessage(responseBody: String): String {
        return try {
            val json = Json.parseToJsonElement(responseBody)
            json.jsonObject["message"]?.jsonPrimitive?.contentOrNull ?: "Unknown Error"
        } catch (e: Exception) {
            "Error parsing JSON: $e"
        }
    }
}