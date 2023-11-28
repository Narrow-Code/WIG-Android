package wig.datastore

object TokenManager {
    private var token: String = ""

    fun setToken(newToken: String) {
        token = newToken
    }

    fun getToken(): String{
        return token
    }
}