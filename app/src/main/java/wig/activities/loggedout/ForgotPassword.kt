package wig.activities.loggedout

import android.os.Bundle
import wig.R
import wig.activities.base.Activity
import wig.managers.EmailManager

class ForgotPassword : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setForgotPasswordBindings()
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