package wig.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import wig.api.users.UserService
import wig.datastore.StoreToken
import wig.datastore.TokenManager
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

        // Disable back press
        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {} })

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
                    TokenManager.setToken(token)
                    apiCall()
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
     */
    private fun apiCall() {
        lifecycleScope.launch {

            try {
                val getLogin = withContext(Dispatchers.IO) {
                    service.postLoginCheck()
                }

                if (getLogin.success) {
                    Log.d("Main", "API SUCCESS")

                    val intent = Intent(this@MainActivity, Scanner::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // TODO make else IF TOKEN IS NOT ACTIVE
                    val storeToken = StoreToken(this@MainActivity)
                    storeToken.saveToken("")

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