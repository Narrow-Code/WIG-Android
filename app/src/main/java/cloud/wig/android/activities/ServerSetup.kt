package cloud.wig.android.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import cloud.wig.android.R
import cloud.wig.android.databinding.ServerSetupBinding

class ServerSetup : AppCompatActivity() {
    private lateinit var binding: ServerSetupBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lock to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Disable back press
        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {} })

        // Set page bindings and open page
        binding = ServerSetupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.connectButton.setOnClickListener {
            val hostname = binding.hostname.text.toString()
            val portNumber = binding.portNumber.text.toString()
            if(hostname == "server" || portNumber == "80") {
                val intent = Intent(this, Scanner::class.java)
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