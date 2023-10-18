package cloud.wig.android.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cloud.wig.android.databinding.ResetPasswordBinding

class ResetPassword : AppCompatActivity() {
    private lateinit var binding: ResetPasswordBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ResetPassword", "onCreate method is executing")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Portrait lock
        val email = intent.getStringExtra("EMAIL_KEY")

        binding = ResetPasswordBinding.inflate(layoutInflater) // set bindings to login_page layout id's
        val view = binding.root
        setContentView(view) // Open login_page view
        binding.email.text = email

        binding.icExit.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

    }
}