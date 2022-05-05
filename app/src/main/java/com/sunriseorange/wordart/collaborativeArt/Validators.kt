package com.sunriseorange.wordart.collaborativeArt

// Taken from FirebaseEmailAuthExample
// Makes sure that the email and password format are both correct given
// our standards (Ex: 8 characters and 1 number)
class Validators {
    fun validEmail(email: String?) : Boolean {
        // if the email is empty return false
        if (email.isNullOrEmpty()) {
            return false
        }

        // General Email Regex (RFC 5322 Official Standard)
        val emailRegex = Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'" +
                "*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x" +
                "5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z" +
                "0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4" +
                "][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z" +
                "0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|" +
                "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")
        // if the email regex conforms then return true else false
        return emailRegex.matches(email)
    }

    fun validPassword(password: String?) : Boolean {
        // if the password is empty return false
        if (password.isNullOrEmpty()) {
            return false
        }

        // Min 8 char, 1 letter, 1 number
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
        // if the password regex conforms then return true else false
        return passwordRegex.matches(password)
    }
}