package com.mentalys.app.ui.specialist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.SpecialistEntity
import com.mentalys.app.data.repository.SpecialistRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class SpecialistViewModel(
    private val repository: SpecialistRepository
) : ViewModel() {

    private val _specialists = MutableLiveData<Resource<List<SpecialistEntity>>>()
    val specialists: LiveData<Resource<List<SpecialistEntity>>> = _specialists

    fun getSpecialists() {
        viewModelScope.launch {
            repository.getConsultations().observeForever { result ->
                _specialists.postValue(result)
            }
        }
    }
}