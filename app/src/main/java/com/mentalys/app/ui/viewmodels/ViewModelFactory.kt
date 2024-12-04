package com.mentalys.app.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.data.repository.ArticleRepository
import com.mentalys.app.data.repository.MentalHistoryRepository
import com.mentalys.app.data.repository.ClinicRepository
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.data.repository.SpecialistRepository
import com.mentalys.app.di.Injection
import com.mentalys.app.ui.article.ArticleViewModel
import com.mentalys.app.ui.auth.AuthViewModel
import com.mentalys.app.ui.mental.history.MentalHistoryViewModel
import com.mentalys.app.ui.mental.test.handwriting.MentalTestHandwritingViewModel
import com.mentalys.app.ui.mental.test.quiz.MentalTestQuizViewModel
import com.mentalys.app.ui.mental.test.voice.MentalTestVoiceViewModel
import com.mentalys.app.ui.clinic.ClinicViewModel
import com.mentalys.app.ui.profile.ProfileViewModel
import com.mentalys.app.ui.specialist.SpecialistViewModel
import com.mentalys.app.utils.SettingsPreferences
import com.mentalys.app.utils.dataStore

class ViewModelFactory(
    private val mainRepository: MainRepository,
    private val articleRepository: ArticleRepository,
    private val mentalTestRepository: MentalTestRepository,
    private val mentalHistoryRepository: MentalHistoryRepository,
    private val specialistRepository: SpecialistRepository,
    private val clinicRepository: ClinicRepository,
    private val preferences: SettingsPreferences
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepository, preferences) as T
        }
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(mainRepository, preferences) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(mainRepository, preferences) as T
        }
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
            return ArticleViewModel(articleRepository) as T
        }
        if (modelClass.isAssignableFrom(ClinicViewModel::class.java)) {
            return ClinicViewModel(clinicRepository) as T
        }
        if (modelClass.isAssignableFrom(GeminiViewModel::class.java)) {
            return GeminiViewModel(mainRepository, preferences) as T
        }
        if (modelClass.isAssignableFrom(MentalTestHandwritingViewModel::class.java)) {
            return MentalTestHandwritingViewModel(mentalTestRepository) as T
        }
        if (modelClass.isAssignableFrom(MentalTestVoiceViewModel::class.java)) {
            return MentalTestVoiceViewModel(mentalTestRepository) as T
        }
        if (modelClass.isAssignableFrom(MentalTestQuizViewModel::class.java)) {
            return MentalTestQuizViewModel(mentalTestRepository) as T
        }
        if (modelClass.isAssignableFrom(MentalHistoryViewModel::class.java)) {
            return MentalHistoryViewModel(mentalHistoryRepository) as T
        }
        if (modelClass.isAssignableFrom(SpecialistViewModel::class.java)) {
            return SpecialistViewModel(specialistRepository) as T
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
                val clinicRepository = Injection.provideClinicsRepository(context)
                val mentalTestRepository = Injection.provideMentalTestRepository()
                val mentalHistoryRepository = Injection.provideMentalHistoryRepository(context)
                val specialistRepository = Injection.provideSpecialistRepository(context)
                val preferences = SettingsPreferences.getInstance(context.dataStore)
                instance ?: ViewModelFactory(
                    mainRepository,
                    articlesRepository,
                    mentalTestRepository,
                    mentalHistoryRepository,
                    clinicRepository,
                    specialistRepository,
                    preferences
                )
            }.also { instance = it }
    }
}