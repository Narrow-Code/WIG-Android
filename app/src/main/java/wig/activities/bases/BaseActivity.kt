package wig.activities.bases

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.supersuman.apkupdater.ApkUpdater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wig.activities.CheckedOut
import wig.activities.EmailVerification
import wig.activities.ForgotPassword
import wig.activities.Inventory
import wig.activities.Login
import wig.activities.ResetPassword
import wig.activities.Scanner
import wig.activities.ServerSetup
import wig.activities.Settings
import wig.activities.Signup
import wig.api.BorrowerService
import wig.api.LocationService
import wig.api.OwnershipService
import wig.api.ScannerService
import wig.api.UserService
import wig.api.dto.CheckoutRequest
import wig.api.dto.CheckoutResponse
import wig.api.dto.CommonResponse
import wig.api.dto.CreateBorrowerResponse
import wig.api.dto.EditLocationRequest
import wig.api.dto.EditOwnershipRequest
import wig.api.dto.GetBorrowersResponse
import wig.api.dto.GetCheckedOutItemsResponse
import wig.api.dto.InventoryResponse
import wig.api.dto.LocationResponse
import wig.api.dto.NewOwnershipRequest
import wig.api.dto.OwnershipResponse
import wig.api.dto.ScanResponse
import wig.api.dto.SearchLocationResponse
import wig.api.dto.SearchOwnershipResponse
import wig.api.dto.SearchRequest
import wig.databinding.CheckedOutBinding
import wig.databinding.EmailVerificationBinding
import wig.databinding.ForgotPasswordBinding
import wig.databinding.InventoryBinding
import wig.databinding.LoginBinding
import wig.databinding.ResetPasswordBinding
import wig.databinding.ScannerBinding
import wig.databinding.ServerSetupBinding
import wig.databinding.SettingsBinding
import wig.databinding.SignupBinding
import wig.utils.SettingsManager
import wig.utils.StoreSettings

open class BaseActivity : AppCompatActivity() {
    protected lateinit var emailVerificationBinding: EmailVerificationBinding
    protected lateinit var forgotPasswordBinding: ForgotPasswordBinding
    protected lateinit var loginBinding: LoginBinding
    protected lateinit var resetPasswordBinding: ResetPasswordBinding
    protected lateinit var scannerBinding: ScannerBinding
    protected lateinit var serverSetupBinding: ServerSetupBinding
    protected lateinit var signupBinding: SignupBinding
    protected lateinit var settingsBinding: SettingsBinding
    protected lateinit var checkedOutBinding: CheckedOutBinding
    protected lateinit var inventoryBinding: InventoryBinding

    val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val scannerService = ScannerService.create()
    private val ownershipService = OwnershipService.create()
    private val locationService = LocationService.create()
    private val borrowerService = BorrowerService.create()
    private val userService = UserService.create()

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

    protected fun setSettingsBindings(){
        settingsBinding = SettingsBinding.inflate(layoutInflater)
        val view = settingsBinding.root
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

    protected fun startActivitySettingsLogin() {
        if(SettingsManager.getIsStartupOnScanner()) {
            val scanner = Intent(this, Scanner::class.java)
            startActivity(scanner)

            finish()
        } else {
            val inventory = Intent(this, Inventory::class.java)
            startActivity(inventory)

            finish()
        }
    }

    protected fun startActivityScanner() {
        val intent = Intent(this, Scanner::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    protected fun startActivitySettings() {
        val intent = Intent(this, Settings::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    protected fun startActivityInventory() {
        val intent = Intent(this, Inventory::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    protected fun startActivityCheckedOut() {
        val intent = Intent(this, CheckedOut::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    protected fun setCheckedOutBindings(){
        checkedOutBinding = CheckedOutBinding.inflate(layoutInflater)
        val view = checkedOutBinding.root
        setContentView(view)
    }

    protected fun setInventoryBindings(){
        inventoryBinding = InventoryBinding.inflate(layoutInflater)
        val view = inventoryBinding.root
        setContentView(view)
    }

    protected fun setScannerBindings(){
        scannerBinding = ScannerBinding.inflate(layoutInflater)
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

    protected suspend fun scanQROwnership(qr: String): OwnershipResponse = withContext(Dispatchers.IO) {
        val posts = scannerService.scanQROwnership(qr)
        posts
    }

    protected suspend fun setItemLocation(ownershipUID: String, locationQR: String): CommonResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.setLocation(ownershipUID, locationQR)
        posts
    }

    protected suspend fun createNewLocation(name: String, locationQR: String): LocationResponse = withContext(
        Dispatchers.IO){
        val posts = locationService.createLocation(name, locationQR)
        posts
    }

    protected suspend fun createNewOwnershipNoItem(newOwnershipRequest: NewOwnershipRequest): OwnershipResponse = withContext(
        Dispatchers.IO){
        val posts = ownershipService.createOwnershipNoItem(newOwnershipRequest)
        posts
    }

    protected suspend fun changeQuantity(changeType: String, amount: Int, ownershipUID: String): OwnershipResponse = withContext(Dispatchers.IO){
        val posts = ownershipService.changeQuantity(changeType, amount, ownershipUID)
        posts
    }

    protected suspend fun unpackLocation(locationUID: String): InventoryResponse = withContext(Dispatchers.IO){
        val posts = locationService.unpackLocation(locationUID)
        posts
    }

    protected suspend fun getBorrowers(): GetBorrowersResponse = withContext(Dispatchers.IO){
        val posts = borrowerService.getBorrowers()
        posts
    }

    protected suspend fun createBorrowers(name: String): CreateBorrowerResponse = withContext(Dispatchers.IO){
        val posts = borrowerService.createBorrower(name)
        posts
    }

    protected suspend fun checkout(borrowerUID: String, ownerships: CheckoutRequest): CheckoutResponse = withContext(Dispatchers.IO){
        val posts = borrowerService.checkout(borrowerUID,ownerships)
        posts
    }

    protected suspend fun checkIn(ownerships: CheckoutRequest): CheckoutResponse = withContext(Dispatchers.IO){
        val posts = borrowerService.checkIn(ownerships)
        posts
    }

    protected suspend fun searchOwnership(searchRequest: SearchRequest): SearchOwnershipResponse = withContext(Dispatchers.IO){
        val posts = ownershipService.searchOwnership(searchRequest)
        posts
    }

    protected suspend fun editOwnership(editOwnershipRequest: EditOwnershipRequest, uid: String): CommonResponse = withContext(Dispatchers.IO){
        val posts = ownershipService.editOwnership(editOwnershipRequest, uid)
        posts
    }

    protected suspend fun getCheckedOutItems(): GetCheckedOutItemsResponse = withContext(Dispatchers.IO){
        val posts = borrowerService.getCheckedOutItems()
        posts
    }

    protected suspend fun searchLocation(searchRequest: SearchRequest): SearchLocationResponse = withContext(Dispatchers.IO){
        val posts = locationService.searchLocation(searchRequest)
        posts
    }

    protected suspend fun locationEdit(editLocationRequest: EditLocationRequest, locationUID: String): CommonResponse = withContext(Dispatchers.IO){
        val posts = locationService.locationEdit(editLocationRequest, locationUID)
        posts
    }

    protected suspend fun returnInventory(): InventoryResponse = withContext(Dispatchers.IO){
        val posts = locationService.returnInventory()
        posts
    }

    protected suspend fun ping(hostname: String, port: String): CommonResponse = withContext(Dispatchers.IO){
        val posts = userService.ping(hostname, port)
        posts
    }

    protected fun removeConfirmation(name: String, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Remove")
        alertDialogBuilder.setMessage("Are you sure you want to remove $name from queue?")

        alertDialogBuilder.setPositiveButton("REMOVE") { dialog, _ ->
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

    protected fun addConfirmation(name: String, callback: (Boolean) -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirm Add")
        alertDialogBuilder.setMessage("Are you sure you want to add $name to queue?")

        alertDialogBuilder.setPositiveButton("Add") { dialog, _ ->
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

    fun performVibration(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            ?: run {
                // handle the case where vibration is not supported
            }
    }

    fun playScanSound(context: Context) {
        val ringtone = RingtoneManager.getRingtone(
            context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        )
        ringtone.play()
    }

    fun checkForUpdates() {
        coroutineScope.launch {
            val updater = ApkUpdater(this@BaseActivity, "https://github.com/WIGteam/WIG-Android/releases/latest")
            updater.threeNumbers = true
            if (updater.isInternetConnection() && updater.isNewUpdateAvailable() == true) {
                scannerBinding.topMenu.appName.setTextColor(Color.YELLOW)
                scannerBinding.topMenu.appName.setOnClickListener { updateButton(updater) }
            }
        }
    }

    private fun updateButton(updater: ApkUpdater) {
        updater.requestDownload()
    }

    suspend fun unpackSettings() = withContext(Dispatchers.IO){
        val vibrateFlow: Flow<Boolean?> = StoreSettings(this@BaseActivity).getIsVibrateEnabled
        vibrateFlow.map { isVibrateEnabled ->
            if (isVibrateEnabled != null) {
                SettingsManager.setIsVibrateEnabled(isVibrateEnabled)
            }
        }.first()

        val soundFlow: Flow<Boolean?> = StoreSettings(this@BaseActivity).getIsSoundEnabled
        soundFlow.map { isSoundEnabled ->
            if (isSoundEnabled != null) {
                SettingsManager.setIsSoundEnabled(isSoundEnabled)
            }
        }.first()

        val startupFlow: Flow<Boolean?> = StoreSettings(this@BaseActivity).getIsStartupOnScanner
        startupFlow.map { isStartupOnScanner ->
            if (isStartupOnScanner != null) {
                SettingsManager.setIsStartupOnScanner(isStartupOnScanner)
            }
        }.first()

        val hostedFlow: Flow<Boolean?> = StoreSettings(this@BaseActivity).getIsHosted
        hostedFlow.map { isHosted ->
            if (isHosted != null) {
                SettingsManager.setIsHosted(isHosted)
            }
        }.first()

        val hostnameFlow: Flow<String?> = StoreSettings(this@BaseActivity).getHostname
        hostnameFlow.map { hostname ->
            if (hostname != null) {
                SettingsManager.setHostname(hostname)
            }
        }.first()

        val portNumberFlow: Flow<String?> = StoreSettings(this@BaseActivity).getPort
        portNumberFlow.map { portNumber ->
            if (portNumber != null) {
                SettingsManager.setPortNumber(portNumber)
            }
        }.first()
    }
}