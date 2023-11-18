package cloud.wig.android.api.users

/**
 * This package provides HTTP route constants for user-related operations in the cloud.wig.android.api.users module.
 */
object HttpRoutes {

    private const val BASE_URL = "http://52.23.166.40:30001"

    const val SIGNUP = "$BASE_URL/user/signup"
    const val SALT = "$BASE_URL/user/salt"
    const val LOGIN = "$BASE_URL/user/login"
    const val LOGIN_CHECK = "$BASE_URL/user/validate"
}