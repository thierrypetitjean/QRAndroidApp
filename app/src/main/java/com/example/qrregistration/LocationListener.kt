package com.example.qrregistration

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

//define the listener
private val locationListener: LocationListener = object : LocationListener {
    override fun onLocationChanged(location: Location) {
//            thetext.text = ("" + location.longitude + ":" + location.latitude)
    }
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}