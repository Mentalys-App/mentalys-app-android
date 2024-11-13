package com.mentalys.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.ArticleRepository
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: ArticleRepository,
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