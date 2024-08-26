package wig.activities.loggedout

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.activities.base.Settings
import wig.managers.EmailManager

class ResetPassword : Settings() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setResetPasswordBindings()
        appendEmailToPage()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        resetPasswordBinding.icExit.setOnClickListener { startActivityLogin() }
        resetPasswordBinding.resendEmail.setOnClickListener{ resendEmail() }
    }

    private fun resendEmail() {
        lifecycleScope.launch {
            api.forgotPassword(EmailManager.getEmail())
        }
    }

    private fun appendEmailToPage() {
        val email = EmailManager.getEmail()
        resetPasswordBinding.email.text = email
    }
}