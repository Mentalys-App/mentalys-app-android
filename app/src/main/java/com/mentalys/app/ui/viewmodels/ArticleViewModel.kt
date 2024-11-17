package com.mentalys.app.ui.viewmodels

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