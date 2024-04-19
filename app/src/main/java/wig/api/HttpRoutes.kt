package wig.api

/**
 * This package provides HTTP route constants.
 */
object HttpRoutes {

    private const val BASE_URL = "http://ec2-18-209-15-108.compute-1.amazonaws.com:30001" // SERVER
    //private const val BASE_URL = "http://192.168.0.201:30001" // LOCAL

    const val PING = "$BASE_URL/ping"

    const val SIGNUP = "$BASE_URL/user/signup"
    const val SALT = "$BASE_URL/user/salt"
    const val LOGIN = "$BASE_URL/user/login"
    const val LOGIN_CHECK = "$BASE_URL/app/validate"

    const val SCAN_BARCODE = "${BASE_URL}/app/scan/barcode"
    const val CHECK_QR = "${BASE_URL}/app/scan/check-qr"
    const val SCAN_QR_LOCATION = "${BASE_URL}/app/scan/qr/location"

    const val SET_OWNERSHIP_LOCATION = "${BASE_URL}/app/ownership/set-location"
    const val CHANGE_OWNERSHIP_QUANTITY = "${BASE_URL}/app/ownership/quantity/"
    const val CREATE_OWNERSHIP = "${BASE_URL}/app/ownership/create"
    const val SEARCH_OWNERSHIP = "${BASE_URL}/app/ownership/search"
    const val EDIT_OWNERSHIP = "${BASE_URL}/app/ownership/edit"

    const val CREATE_LOCATION = "${BASE_URL}/app/location/create"
    const val UNPACK_LOCATION = "${BASE_URL}/app/location/unpack"
    const val SEARCH_LOCATION = "${BASE_URL}/app/location/search"
    const val EDIT_LOCATION = "${BASE_URL}/app/location/edit"

    const val GET_BORROWERS = "${BASE_URL}/app/borrower/get"
    const val CREATE_BORROWER = "${BASE_URL}/app/borrower/create"
    const val CHECKOUT = "${BASE_URL}/app/borrower/checkout"
    const val CHECK_IN = "${BASE_URL}/app/borrower/checkin"
    const val GET_CHECKED_OUT = "${BASE_URL}/app/borrower/getcheckedout"

    const val RETURN_INVENTORY = "${BASE_URL}/app/inventory"
}
