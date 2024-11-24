package com.mentalys.app.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mentalys.app.data.ArticleRepository
import com.mentalys.app.di.Injection
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore

class ViewModelFactory(
    private val articleRepository: ArticleRepository,
    private val settingsPreferences: SettingsPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(articleRepository, settingsPreferences) as T
        }
        if (modelClass.isAssignableFrom(QuizTestViewModel::class.java)) {
            return QuizTestViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val repository = Injection.provideRepository(context)
                val preferences = SettingsPreferences.getInstance(context.dataStore)
                instance ?: ViewModelFactory(repository, preferences)
            }.also { instance = it }
    }
}