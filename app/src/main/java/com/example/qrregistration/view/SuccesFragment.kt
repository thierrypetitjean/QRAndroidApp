package com.example.qrregistration.view

import android.widget.TextView
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast
import androidx.navigation.Navigation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.qrregistration.R
import com.example.qrregistration.anims.Anims
import com.example.qrregistration.viewmodel.SuccesViewModel

class SuccesFragment : Fragment() {
    private var loggedInUserTextView: TextView? = null
    private var tvDate: TextView? = null
    private var tvTime: TextView? = null
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

            } else {
                logOutButton!!.isEnabled = false
            }
        }

        successViewModel!!.loggedOutLiveData.observe(this) { loggedOut ->
            if (loggedOut) {
                Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_loggedInFragment_to_loginRegisterFragment)
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
        val anims = Anims()
        anims.bounce(requireContext(), ivMain!!)
        ivMain!!.setOnClickListener { anims.wobble(requireContext(), ivMain!!) }
        logOutButton = view.findViewById(R.id.fragment_loggedin_logOut)
        logOutButton!!.setOnClickListener{ successViewModel!!.logOut() }
        return view
    }
}