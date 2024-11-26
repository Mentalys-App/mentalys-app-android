package com.mentalys.app.data.remote.response.auth

data class RegisterResponse(
    val status: String,
    val message: String,
    val data: RegisterData? = null,
    val errors: List<RegisterError>? = null
)

data class RegisterData(
    val uid: String,
    val email: String
)

data class RegisterError(
    val field: String,
    val message: String
)
