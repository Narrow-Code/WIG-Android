package wig.activities

import android.content.Intent
import android.os.Bundle
import wig.R
import wig.databinding.ForgotPasswordBinding

class ForgotPassword : BaseActivity() {
    private lateinit var binding: ForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setKeyBindings()
        disableBackPress()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.send.setOnClickListener { sendClick() }
        binding.icExit.setOnClickListener { exitClick() }
    }

    private fun setKeyBindings(){
        binding = ForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun exitClick() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun sendClick() {
        // TODO add functionality
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

}