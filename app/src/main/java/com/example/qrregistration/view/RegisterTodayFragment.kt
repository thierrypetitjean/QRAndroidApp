package com.example.qrregistration.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import android.widget.*
import androidx.core.app.ActivityCompat
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

class RegisterTodayFragment : Fragment() {
    private var logOutButton: Button? = null
    private var ivQr: ImageView? = null
    private var flLoading: FrameLayout? = null
    private var registerTodayViewModel: RegisterTodayViewModel? = null
    private lateinit var barcodeInfo: TextView
    private lateinit var cameraView: SurfaceView
    private lateinit var cameraSource: CameraSource
    private val PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerTodayViewModel = ViewModelProvider(this).get(RegisterTodayViewModel::class.java)
        registerTodayViewModel!!.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                logOutButton!!.isEnabled = true
                ivQr!!.setOnClickListener {
                    registerTodayViewModel!!.registerToday(firebaseUser.displayName!!,firebaseUser.email!!, DateHelper().giveDate()!!, DateHelper().giveTime()!!)
                }
                registerTodayViewModel!!.checkToday(DateHelper().giveDate()!!)
            } else {
                logOutButton!!.isEnabled = false
            }
        }
        registerTodayViewModel!!.loggedOutLiveData.observe(this) { loggedOut ->
            if (loggedOut) {
                Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_registerTodayFragment_to_RegisterUserFragment)
            }
        }
        registerTodayViewModel!!.alreadyRegisterTodayLiveData.observe(this){ alreadyToday ->
            if(alreadyToday != null)
            {
                val date = registerTodayViewModel!!.getDateToday()
                val time = registerTodayViewModel!!.getTimeToday()
                //Go to succes page
                val bundle = bundleOf("date" to date, "time" to time)
                Navigation.findNavController(requireView()).navigate(R.id.action_registerTodayFragment_to_loggedInFragment, bundle)

                Toast.makeText(context, "User Already Registerd Today", Toast.LENGTH_SHORT).show()
//                Navigation.findNavController(requireView())
//                    .navigate(action)

            }
            else
            {
                //Hide loading screen
                flLoading!!.visibility = View.GONE
            }
        }

    }

    private fun startCamera()
    {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraSource.start(cameraView.holder)
            return
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
                try {
                    if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Log.e("test", getString(R.string.no_permission))
                        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
                        return
                    }
                    else
                    {
                        startCamera()
                        return
                    }
                } catch (ie: IOException) {
                    Log.e("test", "" + ie.localizedMessage)
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {

                    Log.d("test","" + barcodes.valueAt(0).displayValue)
                    if(barcodes.valueAt(0).displayValue == getString(R.string.qr_validate)) {

                    }
                    else
                    {

                    }
                    barcodeInfo.post { barcodeInfo.text = barcodes.valueAt(0).displayValue + "\n" + DateHelper().giveDate() }
                }
            }
        })

        logOutButton!!.setOnClickListener{
            registerTodayViewModel!!.logOut()
        }
        return view
    }
}