package wig.activities.loggedout

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.base.Settings
import wig.managers.EmailManager

class EmailVerification : Settings() {
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
        emailVerificationBinding.resendEmail.setOnClickListener{ resendEmail() }
    }

    private fun resendEmail() {
        lifecycleScope.launch {
            api.resendVerification(EmailManager.getEmail())
        }
    }

    private fun appendEmailToPage() {
        val email = EmailManager.getEmail()
        emailVerificationBinding.email.text = email
    }

}