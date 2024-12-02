package com.mentalys.app.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.data.repository.ArticlesRepository
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.di.Injection
import com.mentalys.app.ui.auth.AuthViewModel
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore

class ViewModelFactory(
    private val mainRepository: MainRepository,
    private val articlesRepository: ArticlesRepository,
    private val mentalTestRepository: MentalTestRepository,
    private val settingsPreferences: SettingsPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepository, settingsPreferences) as T
        }
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(mainRepository, settingsPreferences) as T
        }
        if (modelClass.isAssignableFrom(ArticlesViewModel::class.java)) {
            return ArticlesViewModel(articlesRepository, settingsPreferences) as T
        }
        if (modelClass.isAssignableFrom(GeminiViewModel::class.java)) {
            return GeminiViewModel(mainRepository, settingsPreferences) as T
        }
        if (modelClass.isAssignableFrom(HandwritingTestViewModel::class.java)) {
            return HandwritingTestViewModel(mentalTestRepository) as T
        }
        if (modelClass.isAssignableFrom(VoiceTestViewModel::class.java)) {
            return VoiceTestViewModel(mentalTestRepository) as T
        }
        if (modelClass.isAssignableFrom(QuizTestViewModel::class.java)) {
            return QuizTestViewModel(mentalTestRepository) as T
        }
        if (modelClass.isAssignableFrom(ReportsViewModel::class.java)) {
            return ReportsViewModel(mentalTestRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val mainRepository = Injection.provideMainRepository(context)
                val articlesRepository = Injection.provideArticlesRepository(context)
                val mentalTestRepository = Injection.provideMentalTestRepository()
                val preferences = SettingsPreferences.getInstance(context.dataStore)
                instance ?: ViewModelFactory(mainRepository, articlesRepository, mentalTestRepository, preferences)
            }.also { instance = it }
    }
}