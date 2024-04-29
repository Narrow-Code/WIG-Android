package wig.activities.loggedout

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.R
import wig.activities.base.Settings
import wig.managers.SettingsManager
import wig.utils.StoreSettings

class ServerSetup : Settings() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setServerSetupBindings()
        setOnClickListeners()
        isHostedCheck()
    }

    private fun setOnClickListeners() {
        serverSetupBinding.connectButton.setOnClickListener { connectClick() }
        serverSetupBinding.disconnectButton.setOnClickListener { disconnectClick() }
        serverSetupBinding.icExit.setOnClickListener { startActivityLogin() }
    }

    private fun isHostedCheck() {
        lifecycleScope.launch {
            if(SettingsManager.getIsHosted()){
                serverSetupBinding.hostname.hint = SettingsManager.getHostname()
                serverSetupBinding.portNumber.hint = SettingsManager.getPortNumber()
                serverSetupBinding.hostname.isEnabled = false
                serverSetupBinding.portNumber.isEnabled = false
                serverSetupBinding.connectButton.visibility = View.INVISIBLE
                serverSetupBinding.disconnectButton.visibility = View.VISIBLE
            } else {
                serverSetupBinding.connectButton.visibility = View.VISIBLE
                serverSetupBinding.disconnectButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun connectClick() {
        val hostname = serverSetupBinding.hostname.text.toString()
        val portNumber = serverSetupBinding.portNumber.text.toString()
        lifecycleScope.launch {
            val response = ping(hostname, portNumber)
            if (response.success) {
                    val storeSettings = StoreSettings(this@ServerSetup)
                    storeSettings.saveIsHosted(true)
                    SettingsManager.setIsHosted(true)
                    storeSettings.saveHostname(hostname)
                    SettingsManager.setHostname(hostname)
                    storeSettings.savePort(portNumber)
                    SettingsManager.setPortNumber(portNumber)
                    serverSetupBinding.connectButton.visibility = View.INVISIBLE
                    serverSetupBinding.disconnectButton.visibility = View.VISIBLE
                    serverSetupBinding.hostname.isEnabled = false
                    serverSetupBinding.portNumber.isEnabled = false
            } else
                serverSetupBinding.error.text = getString(R.string.wig_server_not_recognized)
        }
    }

    private fun disconnectClick() {
        lifecycleScope.launch {
                val storeSettings = StoreSettings(this@ServerSetup)
                storeSettings.saveIsHosted(false)
                SettingsManager.setIsHosted(false)
                storeSettings.saveHostname("")
                SettingsManager.setHostname("")
                storeSettings.savePort("")
                SettingsManager.setPortNumber("")
                serverSetupBinding.connectButton.visibility = View.VISIBLE
                serverSetupBinding.disconnectButton.visibility = View.INVISIBLE
                serverSetupBinding.hostname.isEnabled = true
                serverSetupBinding.portNumber.isEnabled = true
        }
    }

}