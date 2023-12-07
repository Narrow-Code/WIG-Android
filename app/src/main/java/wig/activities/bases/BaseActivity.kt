package wig.activities.bases

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import wig.activities.EmailVerification
import wig.activities.ForgotPassword
import wig.activities.Login
import wig.activities.ResetPassword
import wig.activities.Scanner
import wig.activities.ServerSetup
import wig.activities.Signup
import wig.api.LocationService
import wig.api.OwnershipService
import wig.api.ScannerService
import wig.api.dto.CommonResponse
import wig.api.dto.LocationResponse
import wig.api.dto.OwnershipResponse
import wig.api.dto.ScanResponse
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
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val scannerService = ScannerService.create()
    private val ownershipService = OwnershipService.create()
    private val locationService = LocationService.create()

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

    protected suspend fun scanBarcode(barcode: String): ScanResponse = withContext(Dispatchers.IO){
        val posts = scannerService.scan(barcode)
        posts
    }

    protected suspend fun checkQR(qr: String): CommonResponse = withContext(Dispatchers.IO){
        val posts = scannerService.checkQR(qr)
        posts
    }

    protected suspend fun scanQRLocation(qr: String): LocationResponse = withContext(Dispatchers.IO){
        val posts = scannerService.scanQRLocation(qr)
        posts
    }

    protected suspend fun setItemLocation(ownershipUID: Int, locationQR: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.setLocation(ownershipUID, locationQR)
        posts
    }

    protected suspend fun createNewLocation(type: String, name: String, locationQR: String): LocationResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.createLocation(type, name, locationQR)
        posts
    }

    protected suspend fun changeQuantity(changeType: String, amount: Int, ownershipUID: Int): OwnershipResponse = withContext(Dispatchers.IO){
        val posts = ownershipService.changeQuantity(changeType, amount, ownershipUID)
        posts
    }

    protected fun deleteConfirmation(name: String, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Deletion")
        alertDialogBuilder.setMessage("Are you sure you want to delete $name?")

        alertDialogBuilder.setPositiveButton("DELETE") { dialog, _ ->
            dialog.dismiss()
            callback(true)
        }

        alertDialogBuilder.setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
            callback(false)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}