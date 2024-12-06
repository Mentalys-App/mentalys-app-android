package com.mentalys.app.data.remote.request.auth

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val confirmPassword: String,
    val birth_date: String? = null,
    val location: String? = null,
    val gender: String? = null
)
