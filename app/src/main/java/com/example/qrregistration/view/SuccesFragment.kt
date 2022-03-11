package com.example.qrregistration.view

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.qrregistration.R
import com.example.qrregistration.anims.Anims
import com.example.qrregistration.viewmodel.SuccesViewModel
import java.util.*


class SuccesFragment : Fragment() {
    private var loggedInUserTextView: TextView? = null
    private var tvDate: TextView? = null
    private var tvTime: TextView? = null
    private var tvLat: TextView? = null
    private var tvLong: TextView? = null
    private var logOutButton: Button? = null
    private var successViewModel: SuccesViewModel? = null
    private var ivMain: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        successViewModel = ViewModelProvider(this).get(SuccesViewModel::class.java)

        successViewModel!!.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                loggedInUserTextView!!.text = "Welkom " + firebaseUser.displayName
                logOutButton!!.isEnabled = true

                tvTime!!.text = arguments?.getString("time")
                tvDate!!.text = arguments?.getString("date")
                tvLat!!.text = arguments?.getDouble("lat").toString()
                tvLong!!.text = arguments?.getDouble("long").toString()

                getCityNameByLatLong(
                    requireArguments().getDouble("lat"),
                    requireArguments().getDouble("long")
                )

            } else {
                logOutButton!!.isEnabled = false
            }
        }

        successViewModel!!.loggedOutLiveData.observe(this) { loggedOut ->
            if (loggedOut) {
                Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_askPermissionFragment_to_loginRegisterFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_success, container, false)
        loggedInUserTextView = view.findViewById(R.id.fragment_loggedin_loggedInUser)
        tvDate = view.findViewById(R.id.tvDate)
        tvTime = view.findViewById(R.id.tvTime)
        ivMain = view.findViewById(R.id.ivMain)
        tvLat = view.findViewById(R.id.tvLat)
        tvLong = view.findViewById(R.id.tvLong)
        val anims = Anims()
        anims.bounce(requireContext(), ivMain!!)
        ivMain!!.setOnClickListener { anims.wobble(requireContext(), ivMain!!) }
        logOutButton = view.findViewById(R.id.fragment_loggedin_logOut)
        logOutButton!!.setOnClickListener { successViewModel!!.logOut() }
        return view
    }

    fun getCityNameByLatLong(lat: Double, long: Double) {
        var geocoder = Geocoder(requireContext(), Locale.getDefault())
        var addresses = geocoder.getFromLocation(lat, long, 1)
        Log.d("adres", "" + addresses)
        tvLat!!.text = addresses[0].locality
        tvLong!!.text = addresses[0].getAddressLine(0)

//        var cityName: String = addresses[0].getAddressLine(0)
//        var stateName: String = addresses[0].getAddressLine(1)
//        var countryName: String = addresses[0].getAddressLine(2)
    }
}