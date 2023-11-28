package wig.activities

import android.annotation.SuppressLint
import android.os.Bundle
import wig.utils.EmailManager

class ResetPassword : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
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