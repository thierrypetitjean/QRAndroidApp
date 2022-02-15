package com.example.qrregistration.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.qrregistration.model.AuthAppRepository
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

class RegisterTodayViewModel(application: Application) : AndroidViewModel(application) {
    private val authAppRepository: AuthAppRepository
    val userLiveData: MutableLiveData<FirebaseUser?>
    val loggedOutLiveData: MutableLiveData<Boolean>
    val alreadyRegisterTodayLiveData: MutableLiveData<Boolean>

    fun registerToday(name: String, email: String, date: String, time: String)
    {
        authAppRepository.registerToday(name, email, date, time)
    }

    fun checkToday(date: String){
        authAppRepository.checkToday(date)
    }

    fun getDateToday(): String? {
        return authAppRepository.currentDate
    }

    fun getTimeToday(): String? {
        return authAppRepository.currentTime
    }

    fun logOut() {
        authAppRepository.logOut()
    }

    init {
        authAppRepository = AuthAppRepository(application)
        userLiveData = authAppRepository.userLiveData
        loggedOutLiveData = authAppRepository.loggedOutLiveData
        alreadyRegisterTodayLiveData = authAppRepository.alreadyLoggedInToday
    }
}