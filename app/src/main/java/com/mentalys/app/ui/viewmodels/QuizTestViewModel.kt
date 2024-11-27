package com.mentalys.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.remote.response.mental_test.QuizResponse
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.data.repository.MentalTestRepository.QuizRequest
import com.mentalys.app.utils.Result
import kotlinx.coroutines.launch

class QuizTestViewModel(
    private val mentalTestRepository: MentalTestRepository
) : ViewModel() {
    private val _answers = MutableLiveData<MutableMap<Int, String>>(mutableMapOf())
    val answers: LiveData<MutableMap<Int, String>> = _answers

    private val _testResult = MutableLiveData<Result<QuizResponse>>()
    val testResult: LiveData<Result<QuizResponse>> get() = _testResult

    fun setAnswer(questionNumber: Int, answer: String) {
        _answers.value?.let { currentAnswers ->
            currentAnswers[questionNumber] = answer
            _answers.value = currentAnswers
        }
    }

    fun quizTest(token: String, quizRequest: QuizRequest) {
        viewModelScope.launch {
            _testResult.value = Result.Loading
            val response = mentalTestRepository.quizTest(token, quizRequest)
            _testResult.value = response
        }
    }

//    fun getAnswer(questionNumber: Int): String? {
//        return _answers.value?.get(questionNumber)
//    }
//
//    fun clearAllAnswers() {
//        _answers.value?.clear()
//        _answers.value = mutableMapOf()
//    }
}