package com.example.qrregistration.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Register(
    var author: String? = "",
    var email: String? = "",
    var register_date: String? = "",
    var register_time: String? = "",
    var lat: String? = "",
    var long: String? = ""
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "author" to author,
            "email" to email,
            "register_date" to register_date,
            "register_time" to register_time,
            "latitude" to lat,
            "longitude" to long
        )
    }
}