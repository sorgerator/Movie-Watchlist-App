package com.example.filmfolio.presentation.util

import android.util.Patterns

object AuthValidators {
    fun validateEmail(email: String): String? {
        val trimmed = email.trim()
        if (trimmed.isBlank()) return "Email cannot be empty"
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()) return "Invalid email format"
        return null
    }
    fun validatePassword(password: String): String? {
        if (password.isBlank()) return "Password cannot be empty"
        if (password.length < 6) return "Password must be at least 6 characters"
        return null
    }
    fun validateFullName(fullName: String): String? {
        if (fullName.isBlank()) return "Full name cannot be empty"
        return null
    }
    fun validateConfirmPassword(
        password: String,
        confirmPassword: String
    ): String? {
        if (confirmPassword.isBlank()) return "Please confirm password"
        if (password != confirmPassword) return "Passwords do not match"
        return null
    }
}
