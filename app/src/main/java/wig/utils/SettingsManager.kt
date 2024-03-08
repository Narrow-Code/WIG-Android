package wig.utils

object SettingsManager {
    private var IsVibrateEnabled: Boolean = true
    private var IsSoundEnabled: Boolean = true
    private var IsStartupOnScanner: Boolean = true

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
}