package wig.activities.loggedout

import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import wig.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wig.activities.base.Activity
import wig.api.UserService
import wig.models.requests.SignupRequest
import wig.managers.EmailManager
import wig.managers.RequirementsManager
import wig.utils.SaltAndHash

class Signup : Activity() {
    private val service = UserService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setSignupBindings()
        setOnClickListeners()
    }

    private fun setOnClickListeners(){
        signupBinding.loginButton.setOnClickListener { startActivityLogin() }
        signupBinding.signupButton.setOnClickListener { signupButton() }
        signupBinding.icSelfHost.setOnClickListener { startActivityServerSetup() }
    }

    private fun signupButton() {
        disableButtons()
        val username = signupBinding.username.text.toString()
        val email = signupBinding.email.text.toString()
        val password = signupBinding.password.text.toString()
        val confirmPassword = signupBinding.confirmPassword.text.toString()
        if (requirementsCheck(username, email, password, confirmPassword)) {
            val salt = SaltAndHash().generateSalt()
            val hash = SaltAndHash().generateHash(password, salt.toHexString())
            lifecycleScope.launch {
                signup(username, email, hash, salt)
            }
        }
    }

    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    private suspend fun signup(username: String, email: String, hash: String, salt: ByteArray) = withContext(Dispatchers.IO){
        val posts = service.signup(SignupRequest(username, email, hash, salt.toHexString()))
        if (posts.success) {
            EmailManager.setEmail(email)
            startActivityEmailVerification()
        } else {
            runOnUiThread {
                signupBinding.error.text = posts.message
                enableButtons()
            }
        }
    }

    private fun requirementsCheck(username: String, email: String, password: String, confirmPassword: String): Boolean {
        if (username == "" || email == "" || password == "" || confirmPassword == "") {
            signupBinding.error.text = getString(R.string.required_fields)
            enableButtons()
            return false
        } else if (password != confirmPassword) {
            signupBinding.error.text = getString(R.string.password_mismatch)
            enableButtons()
            return false
        } else if (!RequirementsManager.getUsernameRegex().matches(username)) {
            signupBinding.error.text = getString(R.string.wrong_username_criteria)
            enableButtons()
            return false
        } else if (!RequirementsManager.getEmailRegex().matches(email)) {
            signupBinding.error.text = getString(R.string.email_not_valid)
            enableButtons()
            return false
        } else if (!RequirementsManager.getPasswordRegex().matches(password)) {
            signupBinding.error.text = getString(R.string.wrong_password_criteria)
            enableButtons()
            return false
        }
        return true
    }

    private fun disableButtons() {
        for (i in 0 until signupBinding.root.childCount) {
            val view = signupBinding.root.getChildAt(i)
            if (view is Button) { view.isEnabled = false }
        }
    }

    private fun enableButtons() {
        for (i in 0 until signupBinding.root.childCount) {
            val view = signupBinding.root.getChildAt(i)
            if (view is Button) { view.isEnabled = true }
        }
    }

}