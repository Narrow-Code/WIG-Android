package cloud.wig.android

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cloud.wig.android.databinding.LoginPageBinding

class LoginPage : AppCompatActivity() {
    private lateinit var binding: LoginPageBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Lock layout to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Set bindings
        binding = LoginPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}