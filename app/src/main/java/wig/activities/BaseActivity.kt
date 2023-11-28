package wig.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import wig.databinding.EmailVerificationBinding
import wig.databinding.ForgotPasswordBinding
import wig.databinding.LoginBinding
import wig.databinding.MainScannerBinding
import wig.databinding.ResetPasswordBinding
import wig.databinding.ServerSetupBinding
import wig.databinding.SignupBinding

open class BaseActivity : AppCompatActivity() {
    protected lateinit var emailVerificationBinding: EmailVerificationBinding
    protected lateinit var forgotPasswordBinding: ForgotPasswordBinding
    protected lateinit var loginBinding: LoginBinding
    protected lateinit var resetPasswordBinding: ResetPasswordBinding
    protected lateinit var scannerBinding: MainScannerBinding
    protected lateinit var serverSetupBinding: ServerSetupBinding
    protected lateinit var signupBinding: SignupBinding


    protected fun disableBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {} })
    }

    @SuppressLint("SourceLockedOrientationActivity")
    protected fun setScreenOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    protected fun startActivityLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    protected fun setLoginBindings(){
        loginBinding = LoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)
    }

    protected fun startActivitySignup() {
        val intent = Intent(this, Signup::class.java)
        startActivity(intent)
        finish()
    }

    protected fun setSignupBindings(){
        signupBinding = SignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)
    }

    protected fun startActivityServerSetup() {
        val intent = Intent(this, ServerSetup::class.java)
        startActivity(intent)
        finish()
    }

    protected fun setServerSetupBindings() {
        serverSetupBinding = ServerSetupBinding.inflate(layoutInflater)
        val view = serverSetupBinding.root
        setContentView(view)
    }

    protected fun startActivityForgotPassword() {
        val intent = Intent(this, ForgotPassword::class.java)
        startActivity(intent)
        finish()
    }

    protected fun setForgotPasswordBindings(){
        forgotPasswordBinding = ForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotPasswordBinding.root
        setContentView(view)
    }

    protected fun startActivityScanner() {
        val intent = Intent(this, Scanner::class.java)
        startActivity(intent)
        finish()
    }

    protected fun setScannerBindings(){
        scannerBinding = MainScannerBinding.inflate(layoutInflater)
        val view = scannerBinding.root
        setContentView(view)
    }

    protected fun startActivityResetPassword() {
        val intent = Intent(this, ResetPassword::class.java)
        startActivity(intent)
        finish()
    }

    protected fun setResetPasswordBindings(){
        resetPasswordBinding = ResetPasswordBinding.inflate(layoutInflater)
        val view = resetPasswordBinding.root
        setContentView(view)
    }

    protected fun startActivityEmailVerification() {
        val intent = Intent(this, EmailVerification::class.java)
        startActivity(intent)
        finish()
    }

    protected fun setEmailVerificationBindings(){
        emailVerificationBinding = EmailVerificationBinding.inflate(layoutInflater)
        val view = emailVerificationBinding.root
        setContentView(view)
    }
}