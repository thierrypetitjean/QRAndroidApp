package com.example.qrregistration.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.qrregistration.model.AuthAppRepository
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val authAppRepository: AuthAppRepository
    val userLiveData: MutableLiveData<FirebaseUser?>
    fun login(email: String, password: String) {
        authAppRepository.login(email, password)
    }

    init {
        authAppRepository = AuthAppRepository(application)
        userLiveData = authAppRepository.userLiveData
    }
}