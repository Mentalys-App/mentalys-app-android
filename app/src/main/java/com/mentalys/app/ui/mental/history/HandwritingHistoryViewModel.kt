package com.mentalys.app.ui.mental.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.HandwritingEntity
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import com.mentalys.app.utils.Result
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HandwritingHistoryViewModel(
    private val repository: MentalTestRepository
) : ViewModel() {

    private val _loadingState = MutableStateFlow<Result<Unit>>(Result.Loading)
    val loadingState = _loadingState.asStateFlow()

    private val _handwriting = MutableLiveData<Resource<List<HandwritingEntity>>>()
    val handwriting: LiveData<Resource<List<HandwritingEntity>>> = _handwriting

    // Get list of articles
    fun getHandwritingHistory(token: String) {
        viewModelScope.launch {
            repository.getHandwritingHistory(token).observeForever { result ->
                _handwriting.postValue(result)
            }
        }
    }

//    fun getHandwritingHistory(
//        token: String,
//        startDate: String? = null,
//        endDate: String? = null,
//        sortBy: String = "timestamp",
//        sortOrder: String = "desc"
//    ): Flow<PagingData<HandwritingTestEntity>> {
//        _loadingState.value = Result.Loading
//        return repository.getHandwritingTests(token, startDate, endDate, sortBy, sortOrder)
//            .cachedIn(viewModelScope)
//            .catch { e ->
//                _loadingState.value = Result.Error(e.message ?: "Unknown error")
//                emit(PagingData.empty())
//            }
//    }
}