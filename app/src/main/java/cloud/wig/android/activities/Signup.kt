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

        // Check verifications
        Log.d("signupButton", "About to start signUp method")
        val verification = signUp(username, email, password, confirmPassword)
        // TODO handle verification
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
     * signUp attempts to create user in database based on field verifications
     * @return String A "Pass" or an error message
     */
    private fun signUp(username: String, email: String, password: String, confirmPassword: String) : String {
        // Check null fields
        if (username == "" || email == "" || password == "" || confirmPassword == "") {
            return getString(R.string.required_fields)
        }
        Log.d("signUp", "null field pass")

        // Check that passwords match
        if (password != confirmPassword) {
            return getString(R.string.password_missmatch)
        }
        Log.d("signUp", "password check passed")

        // TODO Check username requirements

        // Check Email validity
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!emailRegex.matches(email)) {
            return getString(R.string.email_not_valid)
        }
        Log.d("signUp", "email passed check")

        // TODO Check password requirements

        // Generate Salt
        val salt = SaltAndHash().generateSalt()

        // Generate Hash
        val hash = SaltAndHash().generateHash(password, salt.toHexString())

        // TODO pass to API

        return "Fail"
    }

    /**
     * Turns salt into Hex String
     * @return Salt as a hex string
     */
    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

}