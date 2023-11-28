package wig.utils

object TokenManager {
    private var token: String = ""

    fun setToken(newToken: String) {
        token = newToken
    }

    fun getToken(): String{
        return token
    }
}

object EmailManager {
    private var email: String = ""

    fun setEmail(newEmail: String) {
        email = newEmail
    }

    fun getEmail(): String{
        return email
    }
}