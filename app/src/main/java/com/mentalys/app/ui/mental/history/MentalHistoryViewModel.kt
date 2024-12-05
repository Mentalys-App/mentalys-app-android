package com.mentalys.app.ui.mental.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.mental.history.HandwritingHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.QuizHistoryEntity
import com.mentalys.app.data.local.entity.mental.history.VoiceHistoryEntity
import com.mentalys.app.data.repository.MentalHistoryRepository
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import com.mentalys.app.utils.Result
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MentalHistoryViewModel(
    private val repository: MentalHistoryRepository
) : ViewModel() {

    private val _handwriting = MutableLiveData<Resource<List<HandwritingHistoryEntity>>>()
    val handwriting: LiveData<Resource<List<HandwritingHistoryEntity>>> = _handwriting

    private val _quiz = MutableLiveData<Resource<List<QuizHistoryEntity>>>()
    val quiz: LiveData<Resource<List<QuizHistoryEntity>>> = _quiz

    private val _voice = MutableLiveData<Resource<List<VoiceHistoryEntity>>>()
    val voice: LiveData<Resource<List<VoiceHistoryEntity>>> = _voice

    fun getHandwritingHistory(token: String) {
        viewModelScope.launch {
            repository.getHandwritingHistory(token).observeForever { result ->
                _handwriting.postValue(result)
            }
        }
    }

    fun getQuizHistory(token: String) {
        viewModelScope.launch {
            repository.getQuizHistory(token).observeForever { result ->
                _quiz.postValue(result)
            }
        }
    }

    fun getVoiceHistory(token: String) {
        viewModelScope.launch {
            repository.getVoiceHistory(token).observeForever { result ->
                _voice.postValue(result)
            }
        }
    }

}