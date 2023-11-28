package wig.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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


/**
 * The Signup class controls the functionality on the login page of the WIG application.
 *
 * @property binding The binding for the Login page layout.
 * @property service An instance of [UserService] for making API calls related to user operations.
 */
class Login : BaseActivity() {
    private lateinit var binding: LoginBinding
    private val service = UserService.create()

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then
     * this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     * Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setKeyBindings()
        disableBackPress()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.loginButton.setOnClickListener { loginButton() }
        binding.signupButton.setOnClickListener { signupButton() }
        binding.icSelfHost.setOnClickListener { selfHostedButton() }
        binding.forgotPassword.setOnClickListener { forgotPasswordButton() }
    }

    private fun setKeyBindings(){
        binding = LoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    /**
     * Handles the login button click event.
     * Retrieves field inputs, sends out a request for the users salt by API call,
     * salts and hashes the password, sends out an API call to login,
     * and retrieves the users UID and authentication token to stay logged in.
     */
    private fun loginButton() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()

        // Disable button
        disableButtons()

        // Check if fields are empty
        if(requirementsCheck(username, password)){
            Log.d("Login", "Before saltAPICall")
            saltAPICall(username, password)
        }
    }

    /**
     * Handles the signup button click event.
     * Starts the [Signup] activity.
     */
    private fun signupButton() {
        val intent = Intent(this, Signup::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Handles the self hosted button click event.
     * Starts the [ServerSetup] activity.
     */
    private fun selfHostedButton() {
        val intent = Intent(this, ServerSetup::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Handles the forgot password button click event.
     * Starts the [ForgotPassword] activity.
     */
    private fun forgotPasswordButton() {
        val intent = Intent(this, ForgotPassword::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * saltAPICall begins the api call process, requesting the salt, salt and hashing the password,
     * and then calling the Login API.
     *
     * @param username The username
     * @param password The password to salt and hash
     */
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
                        binding.error.text = posts.message
                    }

            } catch(e: Exception) {
                // TODO handle exception, maybe network issue popup?
            }

        }
    }

    /**
     *loginAPICall sends the username and hash to the login API,
     * if a success it saves the returned token and UID and redirects to the main scanner page.
     *
     * @param username The username
     * @param hash The hashed password
     */
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

                    // Redirect to scanner page
                    val intent = Intent(this@Login, Scanner::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // Enable button
                    enableButtons()

                    // Set error message
                    binding.error.text = posts.message
                }

            } catch(e: Exception) {
                // TODO handle exception, maybe network issue popup?
            }
        }
    }

    /**
     * Checks the requirements for username, email, and password.
     *
     * @param username The username
     * @param password The password
     * @return True if all requirements are met, false otherwise.
     */
    private fun requirementsCheck(username: String, password: String): Boolean {
        if(username == "" || password == ""){
            binding.error.text = getString(R.string.required_fields)
            enableButtons()
            return false
        }
        return true
    }

    /**
     * Disables all of the buttons on the page.
     */
    private fun disableButtons() {
        for (i in 0 until binding.root.childCount) {
            val view = binding.root.getChildAt(i)
            if (view is Button) {
                view.isEnabled = false
            }
        }
    }

    /**
     * Enables all of the buttons on the page.
     */
    private fun enableButtons() {
        for (i in 0 until binding.root.childCount) {
            val view = binding.root.getChildAt(i)
            if (view is Button) {
                view.isEnabled = true
            }
        }
    }

}