package com.mentalys.app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.remote.response.profile.ProfileResponse
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(
    private val repository: MainRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {

    fun getProfile(token: String): LiveData<Resource<ProfileResponse>> {
        return repository.getProfile(token)
    }

    fun updateProfile(
        token: String,
        username: String?,
        fullName: String?,
        birthDate: String?,
        location: String?,
        gender: String?,
        profilePicFile: File? // Optional
    ): LiveData<Resource<ProfileResponse>> {
        return repository.updateProfile(
            token,
            username,
            fullName,
            birthDate,
            location,
            gender,
            profilePicFile
        )
    }

    fun saveProfileSession(
        fullName: String,
        birthDate: String,
        username: String,
        gender: String,
        location: String,
        profilePic: String,
        uid: String,
        createdAt: String,
        updatedAt: String,
    ) {
        viewModelScope.launch {
            preferences.saveFullNameSetting(fullName)
            preferences.saveBirthDateSetting(birthDate)
            preferences.saveUsernameSetting(username)
            preferences.saveGenderSetting(gender)
            preferences.saveLocationSetting(location)
            preferences.saveProfilePicSetting(profilePic)
            preferences.saveUidSetting(uid)
            preferences.saveCreatedAtSetting(createdAt)
            preferences.saveUpdatedAtSetting(updatedAt)

        }
    }


    fun deleteProfileSession() {
        viewModelScope.launch {
            preferences.deleteUpdatedAtSetting()
            preferences.deleteCreatedAtSetting()
            preferences.deleteBirthDateSetting()
            preferences.deleteGenderSetting()
            preferences.deleteUsernameSetting()
            preferences.deleteProfilePicSetting()
            preferences.deleteLocationSetting()
            preferences.deleteFullNameSetting()
        }
    }

}
