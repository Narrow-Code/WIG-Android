package wig.activities.base

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

// Bindings sets up all of the layout bindings
open class Bindings : API() {
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

    // setLoginBindings sets up the Login layout bindings
    protected fun setLoginBindings(){
        loginBinding = LoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)
    }

    // setSignupBindings sets up the Signup layout bindings
    protected fun setSignupBindings(){
        signupBinding = SignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)
    }

    // setSettingsBindings sets up the Settings layout bindings
    protected fun setSettingsBindings(){
        settingsBinding = SettingsBinding.inflate(layoutInflater)
        val view = settingsBinding.root
        setContentView(view)
    }

    // setForgotPasswordBindings sets up the ForgotPassword layout bindings
    protected fun setForgotPasswordBindings(){
        forgotPasswordBinding = ForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotPasswordBinding.root
        setContentView(view)
    }

    // setCheckedOutBindings sets up the CheckedOut layout bindings
    protected fun setCheckedOutBindings(){
        checkedOutBinding = CheckedOutBinding.inflate(layoutInflater)
        val view = checkedOutBinding.root
        setContentView(view)
    }

    // setInventoryBindings sets up the Inventory layout bindings
    protected fun setInventoryBindings(){
        inventoryBinding = InventoryBinding.inflate(layoutInflater)
        val view = inventoryBinding.root
        setContentView(view)
    }

    // setScannerBindings sets up the Scanner layout bindings
    protected fun setScannerBindings(){
        scannerBinding = ScannerBinding.inflate(layoutInflater)
        val view = scannerBinding.root
        setContentView(view)
    }

    // setResetPasswordBindings sets up the ResetPassword layout bindings
    protected fun setResetPasswordBindings(){
        resetPasswordBinding = ResetPasswordBinding.inflate(layoutInflater)
        val view = resetPasswordBinding.root
        setContentView(view)
    }

    // setEmailVerificationBindings sets up the EmailVerification layout bindings
    protected fun setEmailVerificationBindings(){
        emailVerificationBinding = EmailVerificationBinding.inflate(layoutInflater)
        val view = emailVerificationBinding.root
        setContentView(view)
    }

    // setServerSetupBindings sets up the ServerSetup layout bindings
    protected fun setServerSetupBindings() {
        serverSetupBinding = ServerSetupBinding.inflate(layoutInflater)
        val view = serverSetupBinding.root
        setContentView(view)
    }
}