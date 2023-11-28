package wig.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import wig.api.UserService
import wig.utils.StoreToken
import wig.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {

    private val service = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        checkStoredDataAndCall()
    }

    private fun checkStoredDataAndCall() {
        lifecycleScope.launch {
            val storeToken = StoreToken(this@MainActivity)
            val tokenFlow: Flow<String?> = storeToken.getToken

            tokenFlow.collect { token ->
                if (!token.isNullOrBlank()) {
                    TokenManager.setToken(token)
                    apiCall()
                } else {
                    startActivityLogin()
                }
            }
        }

    }

    private fun apiCall() {
        lifecycleScope.launch {

            try {
                val getLogin = withContext(Dispatchers.IO) {
                    service.validate()
                }

                if (getLogin.success) {
                    startActivityScanner()
                } else {
                    // TODO make else IF TOKEN IS NOT ACTIVE
                    val storeToken = StoreToken(this@MainActivity)
                    storeToken.saveToken("")

                    startActivityLogin()
                }
            } catch (e: Exception) {
                // TODO handle exception, maybe network issue popup?
            }
        }
    }

}