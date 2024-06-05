package wig.api

import wig.managers.SettingsManager

/**
 * This package provides HTTP route constants.n
 */
object HttpRoutes {

    // private const val DEFAULT_BASE_URL = "http://ec2-18-209-15-108.compute-1.amazonaws.com:30001" // SERVER
    private const val DEFAULT_BASE_URL = "http://192.168.0.201:30001"
    val BASE_URL: String = getBaseUrl()

    val SIGNUP = "$BASE_URL/user/signup"
    val LOGIN = "$BASE_URL/user/login"
    val LOGIN_CHECK = "$BASE_URL/app/validate"

    val SCAN_BARCODE = "${BASE_URL}/app/scan/barcode"
    val CHECK_QR = "${BASE_URL}/app/scan/check-qr"
    val SCAN_QR_LOCATION = "${BASE_URL}/app/scan/qr/location"
    val SCAN_QR_OWNERSHIP = "${BASE_URL}/app/scan/qr/ownership"

    val OWNERSHIP = "${BASE_URL}/app/ownership"
    val SEARCH_OWNERSHIP = "${BASE_URL}/app/ownership/search"

    val LOCATION = "${BASE_URL}/app/location"
    val SEARCH_LOCATION = "${BASE_URL}/app/location/search"

    val BORROWER = "${BASE_URL}/app/borrower"
    val CHECKOUT = "${BASE_URL}/app/borrower/checkout"
    val CHECK_IN = "${BASE_URL}/app/borrower/checkin"
    val GET_CHECKED_OUT = "${BASE_URL}/app/borrower/getcheckedout"

    val RETURN_INVENTORY = "${BASE_URL}/app/inventory"

    private fun getBaseUrl(): String {
        return if (SettingsManager.getIsHosted()) {
            SettingsManager.getHostname() + ":" + SettingsManager.getPortNumber()
        } else {
            DEFAULT_BASE_URL
        }
    }
}
