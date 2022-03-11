package com.example.qrregistration.view

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.qrregistration.DateHelper
import com.example.qrregistration.R
import com.example.qrregistration.StartActivity
import com.example.qrregistration.viewmodel.RegisterTodayViewModel
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.fragment_success.*
import java.io.IOException

class RegisterTodayFragment : Fragment(), LocationListener {
    private var logOutButton: Button? = null
    private var ivQr: ImageView? = null
    private var flLoading: FrameLayout? = null
    private var registerTodayViewModel: RegisterTodayViewModel? = null
    private lateinit var barcodeInfo: TextView
    private lateinit var cameraView: SurfaceView
    private lateinit var cameraSource: CameraSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerTodayViewModel = ViewModelProvider(this).get(RegisterTodayViewModel::class.java)
        registerTodayViewModel!!.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                logOutButton!!.isEnabled = true
                ivQr!!.setOnClickListener {
                    if (registerTodayViewModel!!.getLocLang() != null && registerTodayViewModel!!.getLocLat() != null) {
                        registerTodayViewModel!!.registerToday(
                            firebaseUser.displayName!!,
                            firebaseUser.email!!,
                            DateHelper().giveDate()!!,
                            DateHelper().giveTime()!!,
                            registerTodayViewModel!!.getLocLat().toString(),
                            registerTodayViewModel!!.getLocLang().toString()
                        )
                    } else {
                        Log.d("location", " lat and long null")
                    }
                }
            } else {
                logOutButton!!.isEnabled = false
            }

        }

        registerTodayViewModel!!.hasSendQR.observe(this) { hasSend ->
            if (hasSend) {
                val date = DateHelper().giveDate()!!
                val time = DateHelper().giveTime()!!
                val lat = registerTodayViewModel!!.getLocLat()
                val long = registerTodayViewModel!!.getLocLang()
                //Go to succes page

                val bundle = bundleOf("date" to date, "time" to time, "lat" to lat, "long" to long)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_registerTodayFragment_to_loggedInFragment, bundle)

            }
        }

        val locationManager =
            requireContext().getSystemService(LOCATION_SERVICE) as LocationManager?
        val locationListener = this
        try {
            // Request location update
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }

        registerTodayViewModel!!.loggedOutLiveData.observe(this) { loggedOut ->
            if (loggedOut) {
                Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_registerTodayFragment_to_RegisterUserFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registertoday, container, false)
        logOutButton = view.findViewById(R.id.btnSignOut)
        ivQr = view.findViewById(R.id.ivQr)
        flLoading = view.findViewById(R.id.flLoading)
        cameraView = view.findViewById(R.id.camera_view) as SurfaceView
        barcodeInfo = view.findViewById(R.id.txtContent) as TextView

        val barcodeDetector = BarcodeDetector.Builder(context)
            .setBarcodeFormats(Barcode.QR_CODE) //QR_CODE)
            .build()

        cameraSource = CameraSource.Builder(context, barcodeDetector)
            .build()

        cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
//                try {
//                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        Log.e("test", getString(R.string.no_permission))
//                        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
//                        return
//                    }
//                    else
//                    {
//                        startCamera()
//                        return
//                    }
//                } catch (ie: IOException) {
//                    Log.e("test", "" + ie.localizedMessage)
//                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }


        })

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraSource.start(cameraView.holder)
        }

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {

                    Log.d("test", "" + barcodes.valueAt(0).displayValue)
                    if (barcodes.valueAt(0).displayValue == getString(R.string.qr_validate)) {

                    } else {

                    }
                    barcodeInfo.post {
                        barcodeInfo.text =
                            barcodes.valueAt(0).displayValue + "\n" + DateHelper().giveDate()
                    }
                }
            }
        })

        logOutButton!!.setOnClickListener {
            registerTodayViewModel!!.logOut()
        }

//        registerTodayViewModel!!.alreadyRegisterTodayLiveData.observe(this){ alreadyToday ->
//            if(alreadyToday != null)
//            {
//                val date = registerTodayViewModel!!.getDateToday()
//                val time = registerTodayViewModel!!.getTimeToday()
//                val lat = registerTodayViewModel!!.getLocLat()
//                val long = registerTodayViewModel!!.getLocLang()
//                //Go to succes page
//
//                val bundle = bundleOf("date" to date, "time" to time, "lat" to lat, "long" to long)
//                Navigation.findNavController(requireView()).navigate(R.id.action_registerTodayFragment_to_loggedInFragment, bundle)
//
//                Toast.makeText(context, "User Already Registerd Today", Toast.LENGTH_SHORT).show()
//
////                Navigation.findNavController(requireView())
////                    .navigate(action)
//
//            }
//            else
//            {
//                val locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager?
//                val locationListener = this
//                try {
//                    // Request location update
//                    locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
//                } catch(ex: SecurityException) {
//                    Log.d("myTag", "Security Exception, no location available")
//                }
//            }
//        }
        return view
    }

    override fun onLocationChanged(p0: Location) {
        Log.d("location", " lat = " + p0.latitude + " long = " + p0.longitude)
//        Toast.makeText(context, "Location loaded!", Toast.LENGTH_SHORT).show()

        registerTodayViewModel!!.setLocLat(p0.latitude)
        registerTodayViewModel!!.setLocLong(p0.longitude)

        flLoading!!.visibility = View.GONE

    }


}