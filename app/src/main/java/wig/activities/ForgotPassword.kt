package wig.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import wig.R
import wig.databinding.ForgotPasswordBinding

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ForgotPasswordBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lock to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Disable back press
        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {} })

        // Set bindings and open page
        binding = ForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // TODO backend functionality
        binding.send.setOnClickListener {
            Log.d("ForgotPassword", "Send button clicked")
            val email = binding.email.text.toString()
            if(email != "") {
                val intent = Intent(this, ResetPassword::class.java)
                intent.putExtra("EMAIL_KEY", email)
                startActivity(intent)
                finish()
            } else {
                binding.error.text = getString(R.string.required_fields)
            }
        }

        binding.icExit.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

    }

}