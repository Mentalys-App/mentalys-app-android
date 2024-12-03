package com.mentalys.app.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.ArticleEntity
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.local.entity.FoodEntity
import com.mentalys.app.data.repository.ArticleRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: ArticleRepository,
//    private val preferences: SettingsPreferences
) : ViewModel() {

    private val _articles = MutableLiveData<Resource<List<ArticleListEntity>>>()
    val articles: LiveData<Resource<List<ArticleListEntity>>> = _articles

    private val _article = MutableLiveData<Resource<ArticleEntity>>()
    val article: LiveData<Resource<ArticleEntity>> = _article

    private val _foods = MutableLiveData<Resource<List<FoodEntity>>>()
    val foods: LiveData<Resource<List<FoodEntity>>> = _foods

    // Get list of articles
    fun getListArticle() {
        viewModelScope.launch {
            repository.getAllArticle().observeForever { result ->
                _articles.postValue(result)
            }
        }
    }

    // Get single article
    fun getArticle(id: String) {
        viewModelScope.launch {
            repository.getArticle(id).observeForever { result ->
                _article.postValue(result)
            }
        }
    }

    // Get foods
    fun getFoods() {
        viewModelScope.launch {
            repository.getAllFood().observeForever { result ->
                _foods.postValue(result)
            }
        }
    }

}