package com.mentalys.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.MainRepository
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.remote.response.auth.RegisterResponse
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: MainRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {

    private val _registerResult = MutableLiveData<Resource<RegisterResponse>>()
    val registerResult: LiveData<Resource<RegisterResponse>> get() = _registerResult

    fun registerUser(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            val result = repository.registerUser(email, password, confirmPassword)
            result.observeForever {
                _registerResult.postValue(it)
            }
        }
    }

}