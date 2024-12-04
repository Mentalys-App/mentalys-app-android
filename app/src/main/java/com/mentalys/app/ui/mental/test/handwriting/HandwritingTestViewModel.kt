package com.mentalys.app.ui.mental.test.handwriting

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.remote.response.mental.test.HandwritingTestResponse
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class HandwritingTestViewModel(
    private val mentalTestRepository: MentalTestRepository
) : ViewModel() {
    private val _testResult = MutableLiveData<Resource<HandwritingTestResponse>>()
    val testResult: LiveData<Resource<HandwritingTestResponse>> get() = _testResult

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    fun setImageUri(uri: Uri) {
        _currentImageUri.value = uri
    }

    fun handwritingTest(token: String, photo: MultipartBody.Part) {
        _testResult.value = Resource.Loading
        viewModelScope.launch {
            val response = mentalTestRepository.testHandwriting(token, photo)
            _testResult.value = response
        }
    }
}