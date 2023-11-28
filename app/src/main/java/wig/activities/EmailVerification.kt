package wig.activities

import android.os.Bundle
import wig.databinding.EmailVerificationBinding
import wig.utils.EmailManager

class EmailVerification : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setEmailVerificationBindings()
        disableBackPress()
        appendEmailToPage()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        emailVerificationBinding.icExit.setOnClickListener { startActivityLogin() }
    }

    private fun appendEmailToPage() {
        val email = EmailManager.getEmail()
        emailVerificationBinding.email.text = email
    }

}