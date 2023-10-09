package cloud.wig.android.api

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class User {

    suspend fun SignupPost(username: String, email: String, hash: String, salt: ByteArray): String
    {
        return withContext(Dispatchers.IO) {
            val url = URL("http://127.0.0.1:30001/users/signup") // Replace with your API endpoint
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val jsonInputString = """
            {
                "username":"$username",
                "email":"$email",
                "hash":"$hash",
                "salt":"$salt"
            }
        """.trimIndent()

            val outputStream = OutputStreamWriter(connection.outputStream)
            outputStream.write(jsonInputString)
            outputStream.flush()

            // TODO add handling for error codes
            val responseCode = connection.responseCode
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            val responseBody = inputStream.use { it.readText() }

            connection.disconnect()

            if (responseCode.toString() != "200") {
                return@withContext "Fail"
            }

            // Parse the JSON response
            val jsonResponse = JSONObject(responseBody)
            return@withContext jsonResponse.optString("message", null)
        }
    }
}