package com.mentalys.app.data.remote.response.auth

data class LoginResponse(
    val status: String?,
    val idToken: String,
    val message: String? = null,
    val data: LoginData? = null,
    val profile: LoginProfile? = null,
)

data class LoginData(
    val uid: String,
    val email: String,
)

data class LoginProfile(
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val phoneNumber: String?,
    val full_name: String?,
    val created_at: String?,
    val uid: String?,
    val birth_date: String? = null,
    val location: String? = null,
    val gender: String? = null
)
