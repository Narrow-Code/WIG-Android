package wig.activities

import android.os.Bundle
import wig.R
import wig.databinding.ForgotPasswordBinding
import wig.utils.EmailManager

class ForgotPassword : BaseActivity() {
    private lateinit var binding: ForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setKeyBindings()
        disableBackPress()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.send.setOnClickListener { sendClick() }
        binding.icExit.setOnClickListener { startActivityLogin() }
    }

    private fun setKeyBindings(){
        binding = ForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun sendClick() {
        // TODO add backend call functionality
        val email = binding.email.text.toString()
        if(email != "") {
            EmailManager.setEmail(email)
            startActivityResetPassword()
        } else {
            binding.error.text = getString(R.string.required_fields)
        }
    }

}