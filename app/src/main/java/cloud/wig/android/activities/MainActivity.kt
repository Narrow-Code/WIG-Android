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

/**
 * The MainActivity class controls the functionality on the startup of the WIG application.
 *
 * @property service An instance of [UserService] for making API calls related to user operations.
 */
class MainActivity : AppCompatActivity() {

    private val service = UserService.create()

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then
     * this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     * Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate method is executing")

         checkStoredDataAndCall()
    }

    /**
     * checkStoredDataAndCall checks to see if a token and UID exist in the DataStore.
     * If they do exist, it makes an API call checking if the token is currently active.
     * If the token is active, the app is redirected to the Scanner page.
     * If there is no token or it is not active, the user is redirected to the Login page.
     */
    private fun checkStoredDataAndCall() {
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

    /**
     * Makes the API call to check the status of the token.
     * If the token is active, the app is redirected to the Scanner page.
     * If the token is not active, the app is redirected to the Login page,
     * and the token is removed from DataStore.
     *
     * @param uid The saved UID
     * @param token The saved token
     */
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
                    // TODO make else IF TOKEN IS NOT ACTIVE
                    val storeToken = StoreToken(this@MainActivity)
                    val storeUserUID = StoreUserUID(this@MainActivity)
                    storeToken.saveToken("")
                    storeUserUID.saveUID("")

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