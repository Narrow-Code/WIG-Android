package wig.utils

object RequirementsManager {
    private val usernameRegex = Regex("^[a-zA-Z0-9_-]{4,20}$")
    private val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d\\s!@#\$%^&*()_+={}\\[\\]:;<>,.?~\\\\-]{8,}\$")
    private val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")

    fun getUsernameRegex(): Regex {
        return usernameRegex
    }

    fun getPasswordRegex(): Regex {
        return passwordRegex
    }

    fun getEmailRegex(): Regex {
        return emailRegex
    }
}