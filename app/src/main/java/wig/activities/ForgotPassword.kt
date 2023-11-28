package wig.activities

import android.os.Bundle
import wig.R
import wig.databinding.ForgotPasswordBinding
import wig.utils.EmailManager

class ForgotPassword : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setForgotPasswordBindings()
        disableBackPress()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        forgotPasswordBinding.send.setOnClickListener { sendClick() }
        forgotPasswordBinding.icExit.setOnClickListener { startActivityLogin() }
    }

    private fun sendClick() {
        // TODO add backend call functionality
        val email = forgotPasswordBinding.email.text.toString()
        if(email != "") {
            EmailManager.setEmail(email)
            startActivityResetPassword()
        } else {
            forgotPasswordBinding.error.text = getString(R.string.required_fields)
        }
    }

}