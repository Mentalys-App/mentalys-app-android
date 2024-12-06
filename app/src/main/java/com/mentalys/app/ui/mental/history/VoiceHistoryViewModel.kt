package com.mentalys.app.ui.mental.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.HandwritingEntity
import com.mentalys.app.data.local.entity.VoiceEntity
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import com.mentalys.app.utils.Result
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VoiceHistoryViewModel(
    private val repository: MentalTestRepository
) : ViewModel() {

    private val _loadingState = MutableStateFlow<Result<Unit>>(Result.Loading)
    val loadingState = _loadingState.asStateFlow()

    private val _voice = MutableLiveData<Resource<List<VoiceEntity>>>()
    val voice: LiveData<Resource<List<VoiceEntity>>> = _voice

    // Get list of articles
    fun getVoiceTestHistory(token: String) {
        viewModelScope.launch {
            repository.getVoiceTestHistory(token).observeForever { result ->
                _voice.postValue(result)
            }
        }
    }
}