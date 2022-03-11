package com.example.qrregistration.viewmodel

import android.app.Activity
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
    val hasSendQR: MutableLiveData<Boolean>

    fun registerToday(name: String, email: String, date: String, time: String, lat: String, long: String)
    {
        authAppRepository.registerToday(name, email, date, time, lat, long)
    }

    fun logOut() {
        authAppRepository.logOut()
    }

    fun getLocLat(): Double? {
        return authAppRepository.locLat
    }

    fun getLocLang(): Double? {
        return authAppRepository.locLong
    }

    fun setLocLat(lat: Double){
        authAppRepository.locLat = lat
    }

    fun setLocLong(long: Double){
        authAppRepository.locLong = long
    }

    init {
        authAppRepository = AuthAppRepository(application)
        userLiveData = authAppRepository.userLiveData
        loggedOutLiveData = authAppRepository.loggedOutLiveData
        alreadyRegisterTodayLiveData = authAppRepository.alreadyLoggedInToday
        hasSendQR = authAppRepository.hasSendQR

    }
}