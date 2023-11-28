package wig.activities

import android.annotation.SuppressLint
import android.os.Bundle
import wig.R

class ServerSetup : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableBackPress()
        setScreenOrientation()
        setServerSetupBindings()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        serverSetupBinding.connectButton.setOnClickListener { connectClick() }
        serverSetupBinding.icExit.setOnClickListener { startActivityLogin() }
    }

    private fun connectClick() {
        val hostname = serverSetupBinding.hostname.text.toString()
        val portNumber = serverSetupBinding.portNumber.text.toString()
        if(hostname == "server" || portNumber == "80") {
            startActivityScanner()
        } else {
            serverSetupBinding.error.text = getString(R.string.required_fields)
        }
    }

}