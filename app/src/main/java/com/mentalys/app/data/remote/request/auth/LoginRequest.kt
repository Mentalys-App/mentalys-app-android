package com.mentalys.app.data.remote.request.auth

data class LoginRequest(
    val email: String,
    val password: String,
)