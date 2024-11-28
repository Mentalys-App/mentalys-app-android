package com.mentalys.app.data.remote.response.profile

data class ProfileResponse(
    val status: String?,
    val data: ProfileData?
)

data class ProfileData(
    val username: String?,
    val fullName: String?,
    val birthDate: String?,
    val location: String?,
    val gender: String?,
    val profilePic: String?,
    val uid: String?,
    val createdAt: String?,
    val updatedAt: String?
)
