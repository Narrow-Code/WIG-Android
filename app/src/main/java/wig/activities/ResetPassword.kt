package wig.activities

import android.annotation.SuppressLint
import android.os.Bundle
import wig.databinding.ResetPasswordBinding
import wig.utils.EmailManager

class ResetPassword : BaseActivity() {
    private lateinit var binding: ResetPasswordBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setKeyBindings()
        appendEmailToPage()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.icExit.setOnClickListener { startActivityLogin() }

    }

    private fun setKeyBindings(){
        binding = ResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun appendEmailToPage() {
        val email = EmailManager.getEmail()
        binding.email.text = email
    }
}