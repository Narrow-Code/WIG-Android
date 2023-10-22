package cloud.wig.android.api.users

/**
 * This package provides HTTP route constants for user-related operations in the cloud.wig.android.api.users module.
 */
object HttpRoutes {

    private const val BASE_URL = "https://dev03.zahite.net"

    const val SIGNUP = "$BASE_URL/users/signup"
    const val SALT = "$BASE_URL/users/salt"
    const val LOGIN = "$BASE_URL/users/login"
}