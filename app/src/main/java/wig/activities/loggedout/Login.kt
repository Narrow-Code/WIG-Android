package wig.activities.loggedout

import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import wig.utils.StoreToken
import wig.managers.TokenManager
import wig.utils.SaltAndHash
import kotlinx.coroutines.launch
import wig.R
import wig.activities.base.Settings

class Login : Settings() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setLoginBindings()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        loginBinding.loginButton.setOnClickListener { loginButton() }
        loginBinding.signupButton.setOnClickListener { startActivitySignup() }
        loginBinding.icSelfHost.setOnClickListener { startActivityServerSetup() }
        loginBinding.forgotPassword.setOnClickListener { startActivityForgotPassword() }
    }

    private fun loginButton() {
        val username = loginBinding.username.text.toString()
        val password = loginBinding.password.text.toString()
        disableButtons()
        if(requirementsCheck(username, password)){
            lifecycleScope.launch {
                val salt = getSalt(username)
                if (salt != ""){
                    val hash = SaltAndHash().generateHash(password, salt)
                    login(username, hash)
                }
            }
        }
    }

    private suspend fun getSalt(username: String): String {
        val posts = api.getSalt(username)
        return if(posts.success){
            posts.salt
        } else {
            enableButtons()
            runOnUiThread {
                loginBinding.error.text = posts.message
            }
            ""
        }
    }

    private suspend fun login(username: String, hash: String) {
            val posts = api.login(username, hash)
            if(posts.success){
                val storeToken = StoreToken(this@Login)
                storeToken.saveToken(posts.token)
                TokenManager.setToken(posts.token)
                unpackSettings()
                startActivitySettingsLogin()
            } else {
                enableButtons()
                loginBinding.error.text = posts.message
            }
        }

    private fun requirementsCheck(username: String, password: String): Boolean {
        if(username == "" || password == ""){
            loginBinding.error.text = getString(R.string.required_fields)
            enableButtons()
            return false
        }
        return true
    }

    private fun disableButtons() {
        for (i in 0 until loginBinding.root.childCount) {
            val view = loginBinding.root.getChildAt(i)
            if (view is Button) {
                view.isEnabled = false
            }
        }
    }

    private fun enableButtons() {
        for (i in 0 until loginBinding.root.childCount) {
            val view = loginBinding.root.getChildAt(i)
            if (view is Button) {
                view.isEnabled = true
            }
        }
    }

}