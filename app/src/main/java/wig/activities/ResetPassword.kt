package wig.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import wig.databinding.ResetPasswordBinding

class ResetPassword : BaseActivity() {
    private lateinit var binding: ResetPasswordBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setKeyBindings()
        appendEmailToPage()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.icExit.setOnClickListener { exitClick() }

    }

    private fun setKeyBindings(){
        binding = ResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun appendEmailToPage() {
        val email = intent.getStringExtra("EMAIL_KEY")
        binding.email.text = email
    }

    private fun exitClick() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}