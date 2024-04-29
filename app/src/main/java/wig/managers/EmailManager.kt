package wig.managers

object EmailManager {
    private var email: String = ""

    fun setEmail(newEmail: String) {
        email = newEmail
    }

    fun getEmail(): String{
        return email
    }
}