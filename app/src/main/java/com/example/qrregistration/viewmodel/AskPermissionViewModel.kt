package com.example.qrregistration.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.qrregistration.model.AuthAppRepository

class AskPermissionViewModel(application: Application) : AndroidViewModel(application) {

    private val authAppRepository: AuthAppRepository
    val hasLocPermission: MutableLiveData<Boolean>
    val hasCamPermission: MutableLiveData<Boolean>

    fun setLocPermission(hasLoc: Boolean)
    {
        authAppRepository.setLocPermission(hasLoc)
    }

    fun setCamPermission(hasCam: Boolean)
    {
        authAppRepository.setCamPermission(hasCam)
    }

    init {
        authAppRepository = AuthAppRepository(application)
        hasLocPermission = authAppRepository.hasLocPermission
        hasCamPermission = authAppRepository.hasCamPermission
    }
}