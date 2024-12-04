package com.mentalys.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mentalys.app.data.local.entity.HandwritingTestEntity
import com.mentalys.app.data.repository.MentalTestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.mentalys.app.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch

class HandwritingTestHistoryViewModel(
    private val repository: MentalTestRepository
) : ViewModel() {

    private val _loadingState = MutableStateFlow<Result<Unit>>(Result.Loading)
    val loadingState = _loadingState.asStateFlow()
    fun getHandwritingHistory(
        token: String,
        startDate: String? = null,
        endDate: String? = null,
        sortBy: String = "timestamp",
        sortOrder: String = "desc"
    ): Flow<PagingData<HandwritingTestEntity>> {
        _loadingState.value = Result.Loading
        return repository.getHandwritingTests(token, startDate, endDate, sortBy, sortOrder)
            .cachedIn(viewModelScope)
            .catch { e ->
                _loadingState.value = Result.Error(e.message ?: "Unknown error")
                emit(PagingData.empty())
            }
    }
}