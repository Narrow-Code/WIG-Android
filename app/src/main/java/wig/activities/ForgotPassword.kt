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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {} })

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