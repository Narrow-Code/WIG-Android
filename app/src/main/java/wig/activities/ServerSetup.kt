package wig.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wig.R
import wig.activities.bases.BaseActivity

class ServerSetup : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setScreenOrientation()
        setServerSetupBindings()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        serverSetupBinding.connectButton.setOnClickListener { connectClick() }
        // TODO disconnectButton onClicklistener
        serverSetupBinding.icExit.setOnClickListener { startActivityLogin() }
    }

    private fun isHostedCheck() {
        // TODO if is hosted
            // TODO set connectButton invisible
            // TODO set disconnectButton visible
        // TODO if is not hosted
            // TODO set connectButton visible
            // TODO set disconnectButton isvisible
    }

    private fun connectClick() {
        val hostname = serverSetupBinding.hostname.text.toString()
        val portNumber = serverSetupBinding.portNumber.text.toString()
        lifecycleScope.launch {
            val response = ping(hostname, portNumber)
            if (response.success) {
                serverSetupBinding.error.text = "Success" // TODO remove line
                // TODO save HOSTED_URL string
                // TODO save IsHosted boolean true
            } else
                serverSetupBinding.error.text = getString(R.string.wig_server_not_recognized)
        }
    }

    private fun disconnectClick() {
        // TODO save isHosted boolean false
        // TODO remove HOSTED_URL string
        // TODO set texts to empty
    }

}