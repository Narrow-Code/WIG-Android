package wig.activities.loggedout

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.R
import wig.activities.base.Settings
import wig.managers.EmailManager

class ForgotPassword : Settings() {
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
        val email = forgotPasswordBinding.email.text.toString()

        if(email == ""){
            forgotPasswordBinding.error.text = getString(R.string.required_fields)

        }

        lifecycleScope.launch {
            val posts = api.forgotPassword(email)
            if (posts.success){
                EmailManager.setEmail(email)
                startActivityResetPassword()
            } else {
                forgotPasswordBinding.error.text = getString(R.string.there_was_an_error_making_request)
            }
        }
    }

}