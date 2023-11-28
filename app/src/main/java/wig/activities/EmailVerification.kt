package wig.activities

import android.os.Bundle
import wig.databinding.EmailVerificationBinding

class EmailVerification : BaseActivity() {
    private lateinit var binding: EmailVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setKeyBindings()
        disableBackPress()
        appendEmailToPage()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.icExit.setOnClickListener { startActivityLogin() }
    }

    private fun appendEmailToPage() {
        val email = intent.getStringExtra("EMAIL_KEY")
        binding.email.text = email
    }

    private fun setKeyBindings(){
        binding = EmailVerificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

}