package com.mentalys.app.ui.mental.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.QuizEntity
import com.mentalys.app.data.local.entity.VoiceEntity
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class QuizHistoryViewModel(
    private val repository: MentalTestRepository
) : ViewModel() {

    private val _loadingState = MutableStateFlow<Result<Unit>>(Result.Loading)
    val loadingState = _loadingState.asStateFlow()

    private val _quiz = MutableLiveData<Resource<List<QuizEntity>>>()
    val quiz: LiveData<Resource<List<QuizEntity>>> = _quiz

    // Get list of articles
    fun getQuizTestHistory(token: String) {
        viewModelScope.launch {
            repository.getQuizTestHistory(token).observeForever { result ->
                _quiz.postValue(result)
            }
        }
    }
}