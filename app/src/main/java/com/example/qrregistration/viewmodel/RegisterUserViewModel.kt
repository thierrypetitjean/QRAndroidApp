package com.example.qrregistration.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.qrregistration.model.AuthAppRepository
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

class RegisterUserViewModel(application: Application) : AndroidViewModel(application) {
    private val authAppRepository: AuthAppRepository
    val userLiveData: MutableLiveData<FirebaseUser?>

    fun register(email: String, password: String, username: String) {
        authAppRepository.register(email, password, username)
    }

    init {
        authAppRepository = AuthAppRepository(application)
        userLiveData = authAppRepository.userLiveData
    }
}