package wig.activities

import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import wig.api.dto.SaltRequest
import wig.utils.StoreToken
import wig.utils.TokenManager
import wig.utils.SaltAndHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wig.R
import wig.api.UserService
import wig.api.dto.LoginRequest
import wig.databinding.LoginBinding

class Login : BaseActivity() {
    private val service = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setLoginBindings()
        disableBackPress()
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

        // Disable button
        disableButtons()

        // Check if fields are empty
        if(requirementsCheck(username, password)){
            saltAPICall(username, password)
        }
    }

    private fun saltAPICall(username: String, password: String){
        lifecycleScope.launch {
            try {
                val posts = withContext(Dispatchers.IO) {
                    service.getSalt(SaltRequest(username))
                }
                    if(posts.success){
                        val hash = SaltAndHash().generateHash(password, posts.salt)


                        // Call Login API
                        loginAPICall(username, hash)

                    } else {
                        // Enable button
                        enableButtons()

                        // Set error message
                        loginBinding.error.text = posts.message
                    }

            } catch(e: Exception) {
                // TODO handle exception, maybe network issue popup?
            }

        }
    }

    private fun loginAPICall(username: String, hash: String) {
        lifecycleScope.launch {
            try {
                val posts = withContext(Dispatchers.IO) {
                    service.login(LoginRequest(username, hash))
                }
                if(posts.success){
                    // Save token & UID
                    val storeToken = StoreToken(this@Login)
                    storeToken.saveToken(posts.token)
                    TokenManager.setToken(posts.token)

                    startActivityScanner()

                } else {
                    // Enable button
                    enableButtons()

                    // Set error message
                    loginBinding.error.text = posts.message
                }

            } catch(e: Exception) {
                // TODO handle exception, maybe network issue popup?
            }
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