package com.mentalys.app.data.remote.request.auth

data class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)
