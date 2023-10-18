package cloud.wig.android.api.users

/**
 * This package provides HTTP route constants for user-related operations in the cloud.wig.android.api.users module.
 *
 * @author Matthew McCaughey
 */
object HttpRoutes {

    private const val BASE_URL = "https://mocki.io/v1"

    const val SIGNUP = "$BASE_URL/2aa68215-aede-43eb-94d7-cf505f70c13d"
    const val SALT = "$BASE_URL/addSaltURL"
    const val LOGIN = "$BASE_URL/5f02136c-e0de-4a54-a603-212f15f14f13"
}