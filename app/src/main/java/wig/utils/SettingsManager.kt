package wig.utils

object SettingsManager {
    private var IsVibrateEnabled: Boolean = true
    private var IsSoundEnabled: Boolean = true
    private var IsStartupOnScanner: Boolean = true
    private var IsHosted: Boolean = true
    private var Hostname: String = ""
    private var PortNumber: String = ""

    fun setIsVibrateEnabled(isVibrateEnabled: Boolean) {
        IsVibrateEnabled = isVibrateEnabled
    }

    fun getIsVibrateEnabled(): Boolean{
        return IsVibrateEnabled
    }

    fun setIsSoundEnabled(isSoundEnabled: Boolean) {
        IsSoundEnabled = isSoundEnabled
    }

    fun getSoundEnabled(): Boolean{
        return IsSoundEnabled
    }

    fun setIsStartupOnScanner(isStartupOnScanner: Boolean) {
        IsStartupOnScanner = isStartupOnScanner
    }

    fun getIsStartupOnScanner(): Boolean{
        return IsStartupOnScanner
    }

    fun getIsHosted(): Boolean{
        return IsHosted
    }

    fun setIsHosted(isHosted: Boolean) {
        IsHosted = isHosted
    }

    fun getHostname(): String{
        return Hostname
    }

    fun setHostname(hostname: String){
        Hostname = hostname
    }

    fun getPortNumber(): String{
        return PortNumber
    }

    fun setPortNumber(portNumber: String){
        PortNumber = portNumber
    }

}