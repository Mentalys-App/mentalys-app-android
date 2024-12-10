package com.mentalys.app.ui.specialist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.SpecialistEntity
import com.mentalys.app.data.remote.response.payment.PaymentChargeResponse
import com.mentalys.app.data.repository.MainRepository
import com.mentalys.app.data.repository.SpecialistRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class SpecialistViewModel(
    private val repository: SpecialistRepository,
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _specialists = MutableLiveData<Resource<List<SpecialistEntity>>>()
    val specialists: LiveData<Resource<List<SpecialistEntity>>> = _specialists

    private val _specialist = MutableLiveData<Resource<SpecialistEntity>>()
    val specialist: LiveData<Resource<SpecialistEntity>> = _specialist

    private val _paymentResult = MutableLiveData<Resource<PaymentChargeResponse>>()
    val paymentResult: LiveData<Resource<PaymentChargeResponse>> = _paymentResult

    fun getSpecialists() {
        viewModelScope.launch {
            repository.getConsultations().observeForever { result ->
                _specialists.postValue(result)
            }
        }
    }

    fun getSpecialist(id: String) {
        viewModelScope.launch {
            repository.getConsultation(id).observeForever { result ->
                _specialist.postValue(result)
            }
        }
    }

    fun paymentCharge(token: String, psychiatristId: String) {
        viewModelScope.launch {
            mainRepository.paymentCharge(token, psychiatristId).observeForever { result ->
                _paymentResult.postValue(result)
            }
        }
    }
}