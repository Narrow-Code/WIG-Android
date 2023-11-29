package wig.api

/**
 * This package provides HTTP route constants.
 */
object HttpRoutes {

    private const val BASE_URL = "http://34.234.73.177:30001" // SERVER

    const val SIGNUP = "$BASE_URL/user/signup"
    const val SALT = "$BASE_URL/user/salt"
    const val LOGIN = "$BASE_URL/user/login"
    const val LOGIN_CHECK = "$BASE_URL/app/validate"

    const val SCAN_BARCODE = "${BASE_URL}/app/scan/barcode"
    const val CHECK_QR = "${BASE_URL}/app/scan/check-qr"
}