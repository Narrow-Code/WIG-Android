package wig.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import wig.api.UserService
import wig.utils.StoreToken
import wig.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {

    private val service = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        lifecycleScope.launch {
            val token = checkForToken()
            if (token){
                validate()
            }
            startActivityLogin()
        }
    }

    private suspend fun checkForToken(): Boolean = withContext(Dispatchers.IO){
        val storeToken = StoreToken(this@MainActivity)
        val tokenFlow: Flow<String?> = storeToken.getToken
            tokenFlow.map { token ->
                if (!token.isNullOrBlank()) {
                    TokenManager.setToken(token)
                    true
                } else {
                    false
                }
        }.first()
    }

    private suspend fun validate() = withContext(Dispatchers.IO){
        val getLogin = service.validate()
        if (getLogin.success) {
            startActivityScanner()
        } else {
            val storeToken = StoreToken(this@MainActivity)
            storeToken.saveToken("")

            startActivityLogin()
        }
    }

}