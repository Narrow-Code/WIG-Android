package cloud.wig.android.activities

import android.util.Log
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cloud.wig.android.api.users.UserService
import cloud.wig.android.api.users.dto.LoginGetRequest
import cloud.wig.android.datastore.StoreToken
import cloud.wig.android.datastore.StoreUserUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val service = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate method is executing")

         storedCheck()
    }

    private fun storedCheck() {
        lifecycleScope.launch {
            val storeToken = StoreToken(this@MainActivity)
            val tokenFlow: Flow<String?> = storeToken.getToken

            tokenFlow.collect { token ->
                if (!token.isNullOrBlank()) {
                    val storeUserUID = StoreUserUID(this@MainActivity)
                    val userUIDFlow: Flow<String?> = storeUserUID.getUID

                    userUIDFlow.collect { uid ->
                        if (!uid.isNullOrBlank()) {
                            // Make API call
                            apiCall(uid, token)
                        } else {
                            val intent = Intent(this@MainActivity, Login::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }

    private fun apiCall(uid: String, token: String) {
        lifecycleScope.launch {

            try {
                val getLogin = withContext(Dispatchers.IO) {
                    service.getLoginUser(LoginGetRequest(uid, token))
                }

                if (getLogin.success) {
                    Log.d("Main", "API SUCCESS")

                    val intent = Intent(this@MainActivity, Scanner::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                // TODO handle exception, maybe network issue popup?
            }
        }
    }

}