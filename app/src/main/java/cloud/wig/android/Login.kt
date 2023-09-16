package cloud.wig.android

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cloud.wig.android.databinding.LoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: LoginBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginPage", "onCreate method is executing")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Portrait lock

        binding = LoginBinding.inflate(layoutInflater) // set bindings to login_page layout id's
        val view = binding.root
        setContentView(view) // Open login_page view

        // TODO backend functionality
        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if(username == "stitchy" && password == "Test123" || username == "solo" && password == "Test123"){
                val intent = Intent(this, MainScanner::class.java)
                startActivity(intent)
                finish()
            } else if(username == "" || password == ""){
                binding.error.text = getString(R.string.empty_login_credentials)
            } else{
                binding.error.text = getString(R.string.invalid_user_password)
            }
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
            finish()
        }

        binding.icSelfHost.setOnClickListener {
            val intent = Intent(this, ServerSetup::class.java)
            startActivity(intent)
            finish()
        }

        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
            finish()
        }
    }
}