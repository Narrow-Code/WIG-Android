package wig.activities.loggedout

import android.os.Bundle
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
    }

    private fun appendEmailToPage() {
        val email = EmailManager.getEmail()
        resetPasswordBinding.email.text = email
    }
}