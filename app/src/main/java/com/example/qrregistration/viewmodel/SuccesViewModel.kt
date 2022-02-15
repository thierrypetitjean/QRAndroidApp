package com.example.qrregistration.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.qrregistration.model.AuthAppRepository
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

class SuccesViewModel(application: Application) : AndroidViewModel(application) {
    private val authAppRepository: AuthAppRepository
    val userLiveData: MutableLiveData<FirebaseUser?>
    val loggedOutLiveData: MutableLiveData<Boolean>
    val date: String? = null

    fun logOut() {
        authAppRepository.logOut()
    }

    init {
        authAppRepository = AuthAppRepository(application)
        userLiveData = authAppRepository.userLiveData
        loggedOutLiveData = authAppRepository.loggedOutLiveData
    }
}