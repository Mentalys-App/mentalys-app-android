package com.mentalys.app.ui.mental

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.remote.response.mental.history.HistoryItem
import com.mentalys.app.data.repository.MentalTestRepository
import com.mentalys.app.utils.Resource
import com.mentalys.app.utils.Result
import kotlinx.coroutines.launch

class ReportsViewModel(private val repository: MentalTestRepository) : ViewModel() {

    private val _historyResult = MutableLiveData<Resource<List<HistoryItem>>>()
    val historyResult: LiveData<Resource<List<HistoryItem>>> get() = _historyResult

    fun fetchHistory(
        token: String,
        page: Int = 1,
        limit: Int = 10,
        startDate: String? = null,
        endDate: String? = null,
        sortBy: String = "timestamp",
        sortOrder: String = "desc"
    ) {
        viewModelScope.launch {
            _historyResult.postValue(Resource.Loading)
            val result = repository.getAllHistory(token, page, limit, startDate, endDate, sortBy, sortOrder)
            _historyResult.postValue(result)
        }
    }
}