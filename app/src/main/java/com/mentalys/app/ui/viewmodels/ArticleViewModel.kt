package com.mentalys.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mentalys.app.data.ArticleRepository
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.remote.response.article.ArticlesResponse
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: ArticleRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {

    private val _article = MutableLiveData<Resource<List<ArticleEntity>>>()
    val article: LiveData<Resource<List<ArticleEntity>>> = _article

//    val articles: LiveData<PagingData<ArticlesResponse>> = repository.getArticles().cachedIn(viewModelScope)

//    private val _tips = MutableLiveData<String>()
//    val tips: LiveData<String> get() = _tips

    private val _tips = MutableLiveData<Resource<String>>()
    val tips: LiveData<Resource<String>> get() = _tips

    fun generateMentalHealthTips(prompt: String) {
        viewModelScope.launch {
            val result = repository.getMentalHealthTips(prompt)
            _tips.value = result.value
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