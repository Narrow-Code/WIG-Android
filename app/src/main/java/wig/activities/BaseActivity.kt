package wig.activities

import android.annotation.SuppressLint
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
}