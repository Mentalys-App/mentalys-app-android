package com.mentalys.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import kotlinx.coroutines.launch

class GeminiViewModel(
    private val repository: MainRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {

    private val _tips = MutableLiveData<Resource<String>>()
    val tips: LiveData<Resource<String>> get() = _tips

    fun generateMentalHealthTips(prompt: String) {
        viewModelScope.launch {
            val result = repository.getMentalHealthTips(prompt)
            result.observeForever { resource ->
                when (resource) {
                    is Resource.Success -> Log.d("ArticleViewModel", "Success: ${resource.data}")
                    is Resource.Error -> Log.e("ArticleViewModel", "Error: ${resource.error}")
                    is Resource.Loading -> Log.d("ArticleViewModel", "Loading...")
                }
                _tips.value = resource
            }
        }
    }

}