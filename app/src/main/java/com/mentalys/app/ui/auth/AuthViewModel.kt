package com.mentalys.app.ui.auth

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.data.remote.response.auth.LoginResponse
import com.mentalys.app.data.remote.response.auth.RegisterResponse
import com.mentalys.app.data.remote.response.auth.ResetPasswordResponse
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: MainRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {

    private val _registerResult = MutableLiveData<Resource<RegisterResponse>>()
    val registerResult: LiveData<Resource<RegisterResponse>> get() = _registerResult

    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> get() = _loginResult

    private val _resetPasswordResult = MutableLiveData<Resource<ResetPasswordResponse>>()
    val resetPasswordResult: LiveData<Resource<ResetPasswordResponse>> get() = _resetPasswordResult

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    fun setImageUri(uri: Uri) {
        _currentImageUri.value = uri
    }

    fun registerUser(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            val result = repository.registerUser(
                firstName = firstName,
                lastName = lastName,
                username = username,
                email = email,
                phoneNumber = phoneNumber,
                password = password,
                confirmPassword = confirmPassword
            )
            result.observeForever {
                _registerResult.postValue(it)
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.loginUser(email, password)
            result.observeForever { data ->
                _loginResult.postValue(data)
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            val result = repository.resetPassword(email)
            result.observeForever {
                _resetPasswordResult.postValue(it)
            }
        }
    }

    fun saveUserLoginSession(uid: String, token: String, email: String, fullName: String, username: String, phoneNumber: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            preferences.saveIsLoginSetting(true)
            preferences.saveUidSetting(uid)
            preferences.saveTokenSetting(token)
            preferences.saveEmailSetting(email)
            preferences.saveFullNameSetting(fullName)
            preferences.saveUsernameSetting(username)
            preferences.savePhoneNumberSetting(phoneNumber)
            preferences.saveFullNameSetting(fullName)
            _isLoggedIn.value = true
            onComplete() // Notify that the operation is complete
        }
    }

    fun deleteLoginSession() {
        viewModelScope.launch {
            // auth session
            preferences.deleteIsLoginSetting()
            preferences.deleteUidSetting()
            preferences.deleteTokenSetting()
            preferences.deleteEmailSetting()
            // profile session
            preferences.deleteUpdatedAtSetting()
            preferences.deleteCreatedAtSetting()
            preferences.deleteBirthDateSetting()
            preferences.deleteGenderSetting()
            preferences.deleteUsernameSetting()
            preferences.deleteProfilePicSetting()
            preferences.deleteLocationSetting()
            preferences.deleteFullNameSetting()
            //
            preferences.deleteIsHaveProfileSetting()
            _isLoggedIn.value = false
        }
    }

}