package com.mentalys.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.ArticlesRepository
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.SettingsPreferences
import kotlinx.coroutines.launch

class ArticlesViewModel(
    private val repository: ArticlesRepository,
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

}