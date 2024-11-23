package com.mentalys.app.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VoiceTestViewModel : ViewModel() {

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
}