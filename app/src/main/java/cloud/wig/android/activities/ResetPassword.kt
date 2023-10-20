package cloud.wig.android.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import cloud.wig.android.databinding.ResetPasswordBinding

class ResetPassword : AppCompatActivity() {
    private lateinit var binding: ResetPasswordBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lock to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val email = intent.getStringExtra("EMAIL_KEY")

        // Disable back press
        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {} })

        // Set bindings to page and open
        binding = ResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Display email on page
        binding.email.text = email

        binding.icExit.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

    }

}