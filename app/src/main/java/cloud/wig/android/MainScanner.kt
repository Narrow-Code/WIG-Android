package cloud.wig.android

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cloud.wig.android.databinding.MainScannerBinding

class MainScanner : AppCompatActivity() {
    private lateinit var binding: MainScannerBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginPage", "onCreate method is executing")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Portrait lock

        binding = MainScannerBinding.inflate(layoutInflater) // set bindings to login_page layout id's
        val view = binding.root
        setContentView(view) // Open login_page view

    }
}