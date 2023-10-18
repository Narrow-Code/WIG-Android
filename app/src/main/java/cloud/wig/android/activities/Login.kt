package cloud.wig.android.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cloud.wig.android.R
import cloud.wig.android.databinding.LoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: LoginBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set page orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Set bindings for Signup Page and Open
        binding = LoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set on click listeners
        binding.loginButton.setOnClickListener {loginButton()}
        binding.signupButton.setOnClickListener {signupButton()}
        binding.icSelfHost.setOnClickListener {selfHostedButton()}
        binding.forgotPassword.setOnClickListener {forgotPasswordButton()}
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

        if(username == "stitchy" && password == "Test123" || username == "solo" && password == "Test123"){
            // TODO have redirect to main page
            binding.error.text = ""
        } else if(username == "" || password == ""){
            binding.error.text = getString(R.string.empty_login_credentials)
        } else{
            binding.error.text = getString(R.string.invalid_user_password)
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
}