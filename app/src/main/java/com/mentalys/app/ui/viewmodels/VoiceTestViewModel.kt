package com.mentalys.app.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.remote.response.mental_test.HandwritingResponse
import com.mentalys.app.data.remote.response.mental_test.VoiceResponse
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class VoiceTestViewModel(private val mentalTestRepository: MentalTestRepository) : ViewModel() {

    private val _testResult = MutableLiveData<Result<VoiceResponse>>()
    val testResult: LiveData<Result<VoiceResponse>> get() = _testResult

    private val _audioFileUri = MutableLiveData<Uri?>()
    val audioFileUri: LiveData<Uri?> = _audioFileUri

    private val _isRecording = MutableLiveData(false)
    val isRecording: LiveData<Boolean> = _isRecording

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    fun setAudioFileUri(uri: Uri?) {
        _audioFileUri.value = uri
    }

    fun setRecordingStatus(isRecording: Boolean) {
        _isRecording.value = isRecording
    }

    fun setPlayingStatus(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    fun voiceTest(token: String, audio: MultipartBody.Part) {
        _testResult.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    mentalTestRepository.testVoice(token, audio)
                _testResult.postValue(response)
            } catch (e: Exception) {
                _testResult.postValue(Result.Error("Exception: ${e.message}"))
            }
        }
    }
}