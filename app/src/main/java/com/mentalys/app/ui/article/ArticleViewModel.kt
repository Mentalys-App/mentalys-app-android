package com.mentalys.app.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.remote.response.article.ArticleListItem
import com.mentalys.app.data.repository.ArticleRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: ArticleRepository,
//    private val preferences: SettingsPreferences
) : ViewModel() {

    private val _articles = MutableLiveData<Resource<List<ArticleListEntity>>>()
    val articles: LiveData<Resource<List<ArticleListEntity>>> = _articles

    private val _article = MutableLiveData<Resource<List<ArticleEntity>>>()
    val article: LiveData<Resource<List<ArticleEntity>>> = _article

    // Get stories
    fun getListArticle() {
        viewModelScope.launch {
            repository.getAllArticle().observeForever { result ->
                _articles.postValue(result)
            }
        }
    }

    // Get stories
//    fun fetchArticle() {
//        viewModelScope.launch {
//            repository.getArticle().observeForever { result ->
//                _article.postValue(result)
//            }
//        }
//    }

}