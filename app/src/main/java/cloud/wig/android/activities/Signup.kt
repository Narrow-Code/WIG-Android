package cloud.wig.android.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cloud.wig.android.R
import cloud.wig.android.databinding.SignupBinding
import cloud.wig.android.models.SaltAndHash

class Signup : AppCompatActivity() {
    private lateinit var binding: SignupBinding
    // TODO set verification rules, maybe in own file
    private val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    private val usernameRegex = Regex(".*")
    private val passwordRegex = Regex(".*")


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SignupPage", "onCreate method is executing")

        // Set page orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Set bindings for Signup Page and Open
        binding = SignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set on click listeners
        binding.loginButton.setOnClickListener {loginButton()}
        binding.signupButton.setOnClickListener {signupButton()}
        binding.icSelfHost.setOnClickListener {selfHostButton()}
    }

    /**
     * signupButton controls the functions of the Signup button.
     */
    private fun signupButton() {
        // Store field inputs as variables
        val username = binding.username.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()

        // Check null fields
        if (username == "" || email == "" || password == "" || confirmPassword == "") {
            binding.error.text = getString(R.string.required_fields)
        }

        // Check that passwords match
        else if (password != confirmPassword) {
            binding.error.text = getString(R.string.password_missmatch)
        }

        // Check username requirements
        else if (!usernameRegex.matches(username)) {
                binding.error.text = getString(R.string.wrong_username_criteria)
        }

        // Check Email validity
        else if (!emailRegex.matches(email)) {
            binding.error.text = getString(R.string.email_not_valid)
        }

        // Check password requirements
            else if(!passwordRegex.matches(password)){
                binding.error.text = getString(R.string.wrong_password_criteria)
            }

        // All local verifications passed
        else {
            // Generate Salt
            val salt = SaltAndHash().generateSalt()

            // Generate Hash
            val hash = SaltAndHash().generateHash(password, salt.toHexString())

            // TODO pass to API
        }

        // TODO if API is success switch to email screen
        // TODO else log error

    }

    /**
     * loginButton controls the functions of the Login button.
     */
    private fun loginButton() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * selfHostButton controls the functions of the self host button.
     */
    private fun selfHostButton() {
        val intent = Intent(this, ServerSetup::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Turns salt into Hex String
     * @return Salt as a hex string
     */
    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

}