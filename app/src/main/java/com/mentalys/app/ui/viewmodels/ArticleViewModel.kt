package com.mentalys.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.ArticleRepository
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: ArticleRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {

    private val _article = MutableLiveData<Resource<List<ArticleEntity>>>()
    val article: LiveData<Resource<List<ArticleEntity>>> = _article

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

    // Get stories
    fun fetchArticle() {
        viewModelScope.launch {
            repository.getArticle().observeForever { result ->
                _article.postValue(result)
            }
        }
    }

    // Themes
    fun getThemeSetting() = preferences.getThemeSetting().asLiveData()
    fun saveThemeSetting(isDark: Boolean) {
        viewModelScope.launch { preferences.saveThemeSetting(isDark) }
    }

    // Notifications
    fun getNotificationSetting() = preferences.getNotificationSetting().asLiveData()
    fun saveNotificationSetting(isEnabled: Boolean) {
        viewModelScope.launch { preferences.saveNotificationSetting(isEnabled) }
    }

}