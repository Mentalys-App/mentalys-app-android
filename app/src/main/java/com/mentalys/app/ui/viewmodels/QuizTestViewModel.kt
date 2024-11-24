package com.mentalys.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizTestViewModel : ViewModel() {
    private val _answers = MutableLiveData<MutableMap<Int, String>>(mutableMapOf())
    val answers: LiveData<MutableMap<Int, String>> = _answers

    fun setAnswer(questionNumber: Int, answer: String) {
        _answers.value?.let { currentAnswers ->
            currentAnswers[questionNumber] = answer
            _answers.value = currentAnswers
        }
    }

    fun getAnswer(questionNumber: Int): String? {
        return _answers.value?.get(questionNumber)
    }

    fun clearAllAnswers() {
        _answers.value?.clear()
        _answers.value = mutableMapOf()
    }
}