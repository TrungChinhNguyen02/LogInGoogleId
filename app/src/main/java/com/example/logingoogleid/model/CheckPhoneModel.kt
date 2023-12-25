package com.example.logingoogleid.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CheckPhoneModel : ViewModel() {

    private val _isValidPhoneNumber = MutableLiveData<Boolean>()
    val isValidPhoneNumber: LiveData<Boolean> = _isValidPhoneNumber

//    kiểm tra xem có hợp lệ hay không
    fun validatePhoneNumber(phoneNumber: String) {
        val regex = "^0\\d{9}$"
        _isValidPhoneNumber.value = phoneNumber.matches(regex.toRegex())
    }
}