package com.mentalys.app.ui.mental.test.voice

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.remote.response.mental.test.VoiceTestResponse
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MentalTestVoiceViewModel(private val mentalTestRepository: MentalTestRepository) : ViewModel() {

    private val _testResult = MutableLiveData<Resource<VoiceTestResponse>>()
    val testResult: LiveData<Resource<VoiceTestResponse>> get() = _testResult

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
        _testResult.value = Resource.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    mentalTestRepository.testVoice(token, audio)
                _testResult.postValue(response)
            } catch (e: Exception) {
                _testResult.postValue(Resource.Error("Exception: ${e.message}"))
            }
        }
    }
}