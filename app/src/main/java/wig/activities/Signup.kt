package wig.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import wig.R
import wig.databinding.SignupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wig.api.UserService
import wig.api.dto.SignupRequest
import wig.utils.SaltAndHash

class Signup : BaseActivity() {

    // Set variables
    private lateinit var binding: SignupBinding
    private val usernameRegex = Regex("^[a-zA-Z0-9_-]{4,20}$")
    private val passwordRegex =
        Regex("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d\\s!@#\$%^&*()_+={}\\[\\]:;<>,.?~\\\\-]{8,}\$")
    private val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
    private val service = UserService.create()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setKeyBindings()
        setOnClickListeners()
    }

    private fun setOnClickListeners(){
        binding.loginButton.setOnClickListener { loginButton() }
        binding.signupButton.setOnClickListener { signupButton() }
        binding.icSelfHost.setOnClickListener { selfHostButton() }
    }

    private fun setKeyBindings(){
        binding = SignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun signupButton() {
        // Disable button
        disableButtons()

        // Store field inputs as variables
        val username = binding.username.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()

        // Check requirements and pass API
        if (requirementsCheck(username, email, password, confirmPassword)) {
            // Generate Salt
            val salt = SaltAndHash().generateSalt()

            // Generate Hash
            val hash = SaltAndHash().generateHash(password, salt.toHexString())

            // Pass to API
            signupAPICall(username, email, hash, salt)
        }

    }

    private fun loginButton() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun selfHostButton() {
        val intent = Intent(this, ServerSetup::class.java)
        startActivity(intent)
        finish()
    }

    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    private fun signupAPICall(username: String, email: String, hash: String, salt: ByteArray) {
        lifecycleScope.launch {
            try {
                val posts = withContext(Dispatchers.IO) {
                    service.signup(SignupRequest(username, email, hash, salt.toHexString()))
                }

                // If API is success switch to email screen
                if (posts.success) {
                    val intent = Intent(this@Signup, EmailVerification::class.java)
                    intent.putExtra("EMAIL_KEY", email)
                    startActivity(intent)
                    finish()
                } else {
                    // Enable button
                    enableButtons()

                    // Set error message
                    binding.error.text = posts.message
                }

            } catch (e: Exception) {
                // TODO handle exception, maybe network issue popup?
            }
        }
    }

    private fun requirementsCheck(username: String, email: String, password: String, confirmPassword: String): Boolean {

        // Check null fields
        if (username == "" || email == "" || password == "" || confirmPassword == "") {
            binding.error.text = getString(R.string.required_fields)
            enableButtons()
            return false
        }

        // Check that passwords match
        else if (password != confirmPassword) {
            binding.error.text = getString(R.string.password_mismatch)
            enableButtons()
            return false
        }

        // Check username requirements
        else if (!usernameRegex.matches(username)) {
            binding.error.text = getString(R.string.wrong_username_criteria)
            enableButtons()
            return false
        }

        // Check Email validity
        else if (!emailRegex.matches(email)) {
            binding.error.text = getString(R.string.email_not_valid)
            enableButtons()
            return false
        }

        // Check password requirements
        else if (!passwordRegex.matches(password)) {
            binding.error.text = getString(R.string.wrong_password_criteria)
            enableButtons()
            return false
        }
        return true
    }

    private fun disableButtons() {
        for (i in 0 until binding.root.childCount) {
            val view = binding.root.getChildAt(i)
            if (view is Button) { view.isEnabled = false }
        }
    }

    private fun enableButtons() {
        for (i in 0 until binding.root.childCount) {
            val view = binding.root.getChildAt(i)
            if (view is Button) { view.isEnabled = true }
        }
    }

}