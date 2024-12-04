package com.mentalys.app.ui.mental.test.quiz

import QuizTestResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.data.repository.MentalTestRepository.QuizRequest
import com.mentalys.app.ui.mental.adapters.QuizItem
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class QuizTestViewModel(
    private val mentalTestRepository: MentalTestRepository
) : ViewModel() {
    private val _answers = MutableLiveData<MutableMap<Int, String>>(mutableMapOf())
    val answers: LiveData<MutableMap<Int, String>> = _answers

    private val _testResult = MutableLiveData<Resource<QuizTestResponse>>()
    val testResult: LiveData<Resource<QuizTestResponse>> get() = _testResult

    fun setAnswer(questionNumber: Int, answer: String) {
        _answers.value?.let { currentAnswers ->
            currentAnswers[questionNumber] = answer
            _answers.value = currentAnswers
        }
    }

    fun quizTest(token: String, quizRequest: QuizRequest) {
        viewModelScope.launch {
            _testResult.value = Resource.Loading
            val response = mentalTestRepository.quizTest(token, quizRequest)
            _testResult.value = response
        }
    }

    fun getAnswersForQuestions(questions: List<QuizItem>): List<QuizItem> {
        val currentAnswers = _answers.value ?: mutableMapOf()
        return questions.map { quizItem ->
            quizItem.selectedAnswer = currentAnswers[quizItem.questionNumber]?.toBoolean()
            quizItem
        }
    }

    fun clearAllAnswers() {
        _answers.value?.clear()
        _answers.value = mutableMapOf()
    }
}