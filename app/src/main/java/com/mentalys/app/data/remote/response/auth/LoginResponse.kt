package com.mentalys.app.data.remote.response.auth

data class LoginResponse(
    val status: String?,
    val message: String? = null,
    val data: LoginData? = null,
)

data class LoginData(
    val uid: String,
    val email: String,
    val idToken: String
)
