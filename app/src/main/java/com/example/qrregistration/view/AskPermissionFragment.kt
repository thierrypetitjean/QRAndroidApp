package com.example.qrregistration.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.qrregistration.R
import com.example.qrregistration.viewmodel.AskPermissionViewModel
import java.io.IOException

class AskPermissionFragment : Fragment() {

    private var askPermissionViewModel: AskPermissionViewModel? = null
    private val PERMISSION_REQUEST_CODE_LOC = 100
    private val PERMISSION_REQUEST_CODE_CAM = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askPermissionViewModel = ViewModelProvider(this).get(
            AskPermissionViewModel::class.java
        )
        askPermissionViewModel!!.hasLocPermission.observe(this) { hasPermission ->
            if (hasPermission) {
                Toast.makeText(context, "Has Loc Permission", Toast.LENGTH_SHORT).show()
                checkCamPermission()
            }
        }
        askPermissionViewModel!!.hasCamPermission.observe(this) { hasPermission ->
            if (hasPermission) {
                Toast.makeText(context, "Has Cam Permission", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_askPermissionFragment_to_loginRegisterFragment)
            }
        }
        checkLocPermission()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ask_permission, container, false)

        return view
    }

    fun checkCamPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.CAMERA,
                    )
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE_CAM
                    )
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE_CAM
                    )
                }
            } else {
                askPermissionViewModel!!.setCamPermission(true)
            }
        } catch (ie: IOException) {
            Log.e("test", "" + ie.localizedMessage)
        }
    }

    fun checkLocPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !==
                PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSION_REQUEST_CODE_LOC
                    )
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSION_REQUEST_CODE_LOC
                    )
                }
            } else {
                askPermissionViewModel!!.setLocPermission(true)
                return
            }

        } catch (ie: IOException) {
            Log.e("test", "" + ie.localizedMessage)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show()
                        checkCamPermission()
                    }
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            200 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.CAMERA
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(
                            requireContext(),
                            "Permission cam Granted",
                            Toast.LENGTH_SHORT
                        ).show()
                        checkCamPermission()
                    }
                } else {
                    Toast.makeText(requireContext(), "Permission cam Denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }

        }
    }

}