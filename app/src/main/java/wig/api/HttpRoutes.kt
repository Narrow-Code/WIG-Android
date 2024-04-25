package wig.api

import wig.utils.SettingsManager

/**
 * This package provides HTTP route constants.
 */
object HttpRoutes {

    private const val DEFAULT_BASE_URL = "http://ec2-18-209-15-108.compute-1.amazonaws.com:30001" // SERVER
    private val BASE_URL: String = getBaseUrl()
    //private const val BASE_URL = "http://192.168.0.201:30001" // LOCAL

    val PING = "$BASE_URL/ping"

    val SIGNUP = "$BASE_URL/user/signup"
    val SALT = "$BASE_URL/user/salt"
    val LOGIN = "$BASE_URL/user/login"
    val LOGIN_CHECK = "$BASE_URL/app/validate"

    val SCAN_BARCODE = "${BASE_URL}/app/scan/barcode"
    val CHECK_QR = "${BASE_URL}/app/scan/check-qr"
    val SCAN_QR_LOCATION = "${BASE_URL}/app/scan/qr/location"
    val SCAN_QR_OWNERSHIP = "${BASE_URL}/app/scan/qr/ownership"

    val SET_OWNERSHIP_LOCATION = "${BASE_URL}/app/ownership/set-location"
    val CHANGE_OWNERSHIP_QUANTITY = "${BASE_URL}/app/ownership/quantity/"
    val CREATE_OWNERSHIP = "${BASE_URL}/app/ownership/create"
    val SEARCH_OWNERSHIP = "${BASE_URL}/app/ownership/search"
    val EDIT_OWNERSHIP = "${BASE_URL}/app/ownership/edit"

    val CREATE_LOCATION = "${BASE_URL}/app/location/create"
    val UNPACK_LOCATION = "${BASE_URL}/app/location/unpack"
    val SEARCH_LOCATION = "${BASE_URL}/app/location/search"
    val EDIT_LOCATION = "${BASE_URL}/app/location/edit"

    val GET_BORROWERS = "${BASE_URL}/app/borrower/get"
    val CREATE_BORROWER = "${BASE_URL}/app/borrower/create"
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
