package com.mentalys.app.ui.consultation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mentalys.app.data.local.entity.ConsultationEntity
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.repository.ConsultationRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class ConsultationViewModel(
    private val repository: ConsultationRepository
) : ViewModel() {

    private val _specialists = MutableLiveData<Resource<List<ConsultationEntity>>>()
    val specialists: LiveData<Resource<List<ConsultationEntity>>> = _specialists

    fun getListArticle() {
        viewModelScope.launch {
            repository.getConsultations().observeForever { result ->
                _specialists.postValue(result)
            }
        }
    }
}