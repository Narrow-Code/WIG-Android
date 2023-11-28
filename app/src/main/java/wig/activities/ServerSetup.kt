package wig.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import wig.R
import wig.databinding.ServerSetupBinding

class ServerSetup : BaseActivity() {
    private lateinit var binding: ServerSetupBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setKeyBindings()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.connectButton.setOnClickListener { connectClick() }
        binding.icExit.setOnClickListener { exitClick() }
    }

    private fun setKeyBindings() {
        binding = ServerSetupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun connectClick() {
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

    private fun exitClick(){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

}