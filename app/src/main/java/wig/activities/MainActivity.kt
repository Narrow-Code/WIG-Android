package wig.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import wig.api.UserService
import wig.utils.StoreToken
import wig.managers.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wig.activities.base.BaseActivity
import android.util.Log

class MainActivity : BaseActivity() {

    private val service = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        lifecycleScope.launch {
            unpackSettings()
            if (checkForToken()){
                if(validate()){
                    startActivitySettingsLogin()
                } else {
                    val storeToken = StoreToken(this@MainActivity)
                    storeToken.saveToken("")
                    startActivityLogin()
                }
            }else{
                startActivityLogin()
            }
        }
    }

    private suspend fun checkForToken(): Boolean = withContext(Dispatchers.IO){
        val tokenFlow: Flow<String?> = StoreToken(this@MainActivity).getToken
            tokenFlow.map { token ->
                if (!token.isNullOrBlank()) {
                    TokenManager.setToken(token)
                    Log.d("Token", TokenManager.getToken())
                    true
                } else {
                    false
                }
        }.first()
    }

    private suspend fun validate(): Boolean = withContext(Dispatchers.IO){
        val getLogin = service.validate()
        getLogin.success
    }
}