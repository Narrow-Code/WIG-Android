package cloud.wig.android

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import cloud.wig.android.databinding.LoginPageBinding

class LoginPage : AppCompatActivity() {
    private lateinit var binding: LoginPageBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginPage", "onCreate method is executing")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Portrait lock

        binding = LoginPageBinding.inflate(layoutInflater) // set bindings to login_page layout id's
        val view = binding.root
        setContentView(view) // Open login_page view

        // TODO backend functionality
        binding.loginPageLoginButton.setOnClickListener {
            val username = binding.loginPageUsername.text.toString()
            val password = binding.loginPagePassword.text.toString()

            if(username == "stitchy" && password == "Test123" || username == "solo" && password == "Test123"){
                // TODO have redirect to main page
                binding.invalidUserPassword.visibility = View.INVISIBLE
            } else{
                binding.invalidUserPassword.visibility = View.VISIBLE
            }

        }
    }
}