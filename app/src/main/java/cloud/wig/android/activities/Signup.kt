package cloud.wig.android.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cloud.wig.android.R
import cloud.wig.android.databinding.SignupBinding
import cloud.wig.android.api.users.UserService
import cloud.wig.android.models.SaltAndHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The Signup class controls the functionality on the signup page of the WIG application.
 *
 * @author Matthew McCaughey
 * @property binding The binding for the Signup page layout.
 * @property usernameRegex Regular expression for username validation.
 * @property passwordRegex Regular expression for password validation.
 * @property emailRegex Regular expression for email validation.
 * @property service An instance of [UserService] for making API calls related to user operations.
 */
class Signup : AppCompatActivity() {

    // Set variables
    private lateinit var binding: SignupBinding
    private val usernameRegex = Regex("^[a-zA-Z0-9_-]{4,20}$")
    private val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d\\s!@#\$%^&*()_+={}\\[\\]:;<>,.?~\\\\-]{8,}\$")
    private val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
    private val service = UserService.create()

    /**
     * Called when the activity is first created.
     *
     * This is where you should do all of your normal static set up: create views, bind data to lists, etc.
     * This method also provides you with a [Bundle] containing the activity's previously saved state, if that state
     * was recorded. Always followed by [onStart].
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then
     * this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     * Note: Otherwise it is null.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
     * Handles the signup button click event.
     * Retrieves field inputs, checks requirements, generates salt and hash,
     * and makes an API call to create a new user.
     */
    private fun signupButton() {
        // Disable button
        binding.signupButton.isEnabled = false

        // Store field inputs as variables
        val username = binding.username.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()

        // Check requirements and pass API
        if(requirementsCheck(username, email, password, confirmPassword)) {
            // Generate Salt
            val salt = SaltAndHash().generateSalt()

            // Generate Hash
            val hash = SaltAndHash().generateHash(password, salt.toHexString())

            // Pass to API
            signupAPICall(username, email, hash, salt)
        }

    }

    /**
     * Handles the login button click event.
     * Starts the [Login] activity.
     */
    private fun loginButton() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Handles the self-host button click event.
     * Starts the [ServerSetup] activity.
     */
    private fun selfHostButton() {
        val intent = Intent(this, ServerSetup::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Converts a byte array to its hexadecimal representation.
     *
     * @return A string representing the hexadecimal value of the byte array.
     */
    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    /**
     * Makes an API call to create a new user.
     * Handles API response, enables the signup button on failure, and displays error messages.
     */
    private fun signupAPICall(username: String, email: String, hash: String, salt: ByteArray){
        lifecycleScope.launch {
            try {
                // Disable button
                binding.signupButton.isEnabled = false

                val posts = withContext(Dispatchers.IO) {
                    service.getUser() // TODO Change to createPost
                }

                // If API is success switch to email screen
                if (posts.success){

                    val intent = Intent(this@Signup, EmailVerification::class.java)
                    intent.putExtra("EMAIL_KEY", email)
                    startActivity(intent)
                    finish()
                }
                else {
                    // Enable button
                    binding.signupButton.isEnabled = true

                    // Set error message
                    binding.error.text = posts.message
                }

            } catch (e: Exception) {
                // TODO handle exception, maybe network issue popup?
            }
        }
    }

    /**
     * Checks the requirements for username, email, and password.
     *
     * @return True if all requirements are met, false otherwise.
     */
    private fun requirementsCheck(username: String, email: String, password: String, confirmPassword: String): Boolean{

        // Check null fields
        if (username == "" || email == "" || password == "" || confirmPassword == "") {
            binding.error.text = getString(R.string.required_fields)
            binding.signupButton.isEnabled = true
            return false
        }

        // Check that passwords match
        else if (password != confirmPassword) {
            binding.error.text = getString(R.string.password_mismatch)
            binding.signupButton.isEnabled = true
            return false
        }

        // Check username requirements
        else if (!usernameRegex.matches(username)) {
            binding.error.text = getString(R.string.wrong_username_criteria)
            binding.signupButton.isEnabled = true
            return false
        }

        // Check Email validity
        else if (!emailRegex.matches(email)) {
            binding.error.text = getString(R.string.email_not_valid)
            binding.signupButton.isEnabled = true
            return false
        }

        // Check password requirements
        else if(!passwordRegex.matches(password)){
            binding.error.text = getString(R.string.wrong_password_criteria)
            binding.signupButton.isEnabled = true
            return false
        }
        return true
    }
}