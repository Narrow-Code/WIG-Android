package wig.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
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

    protected fun startActivitySignup() {
        val intent = Intent(this, Signup::class.java)
        startActivity(intent)
        finish()
    }

    protected fun startActivityServerSetup() {
        val intent = Intent(this, ServerSetup::class.java)
        startActivity(intent)
        finish()
    }

    protected fun startActivityForgotPassword() {
        val intent = Intent(this, ForgotPassword::class.java)
        startActivity(intent)
        finish()
    }

    protected fun startActivityScanner() {
        val intent = Intent(this, Scanner::class.java)
        startActivity(intent)
        finish()
    }

    protected fun startActivityResetPassword() {
        val intent = Intent(this, ResetPassword::class.java)
        startActivity(intent)
        finish()
    }

    protected fun startActivityEmailVerification() {
        val intent = Intent(this, EmailVerification::class.java)
        startActivity(intent)
        finish()
    }
}