package cloud.wig.android.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cloud.wig.android.R
import cloud.wig.android.databinding.ForgotPasswordBinding

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ForgotPasswordBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ForgotPassword", "onCreate method is executing")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Portrait lock

        binding = ForgotPasswordBinding.inflate(layoutInflater) // set bindings to login_page layout id's
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