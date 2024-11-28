package com.mentalys.app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.MainRepository
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

    fun registerUser(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            val result = repository.registerUser(email, password, confirmPassword)
            result.observeForever {
                _registerResult.postValue(it)
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.loginUser(email, password)
            result.observeForever {
                _loginResult.postValue(it)
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

    fun saveUserLoginSession(uid: String, token: String, email: String) {
        viewModelScope.launch {
            preferences.saveUidSetting(uid)
            preferences.saveTokenSetting(token)
            preferences.saveEmailSetting(email)
            _isLoggedIn.value = true
        }
    }

    fun deleteLoginSession() {
        viewModelScope.launch {
//            preferences.saveLoginSession(false)
            preferences.deleteUidSetting()
            preferences.deleteTokenSetting()
            preferences.deleteEmailSetting()
            _isLoggedIn.value = false
        }
    }

}