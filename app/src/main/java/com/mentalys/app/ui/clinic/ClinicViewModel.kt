package com.mentalys.app.ui.clinic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.ArticleListEntity
import com.mentalys.app.data.local.entity.ClinicEntity
import com.mentalys.app.data.repository.ClinicRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class ClinicViewModel(
    private val repository: ClinicRepository
) : ViewModel() {
    private val _clinics = MutableLiveData<Resource<List<ClinicEntity>>>()
    val clinics: LiveData<Resource<List<ClinicEntity>>> = _clinics

    fun getListClinics(lat: Number, lng: Number) {
        viewModelScope.launch {
            repository.getNearbyClinic(lat, lng).observeForever { result ->
                _clinics.postValue(result)
            }
        }
    }

    fun getList4Clinics(lat: Number, lng: Number) {
        viewModelScope.launch {
            repository.get4NearbyClinic(lat, lng).observeForever { result ->
                _clinics.postValue(result)
            }
        }
    }
}