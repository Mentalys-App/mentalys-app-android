package com.mentalys.app.ui.mental.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.repository.ArticleRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class MentalTestResultViewModel(
    private val repository: ArticleRepository,
//    private val preferences: SettingsPreferences
) : ViewModel() {

    private val _mentalStateArticles = MutableLiveData<Resource<List<ArticleListEntity>>>()
    val mentalStateArticles: LiveData<Resource<List<ArticleListEntity>>> = _mentalStateArticles

    // Get list of articles
    fun getMentalStateArticle(mentalState: String) {
        viewModelScope.launch {
            repository.getMentalStateArticle(mentalState).observeForever { result ->
                _mentalStateArticles.postValue(result)
            }
        }
    }

    fun getAllArticle() {
        viewModelScope.launch {
            repository.getAllArticle().observeForever { result ->
                _mentalStateArticles.postValue(result)
            }
        }
    }
}