package com.example.qrregistration.view

import android.widget.EditText
import com.example.qrregistration.viewmodel.LoginRegisterViewModel
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.qrregistration.R

class LoginRegisterFragment : Fragment() {
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var loginButton: Button? = null
    private var registerButton: Button? = null
    private var loginRegisterViewModel: LoginRegisterViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginRegisterViewModel = ViewModelProvider(this).get(
            LoginRegisterViewModel::class.java
        )
        loginRegisterViewModel!!.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_loginRegisterFragment_to_registerTodayFragment2)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loginregister, container, false)
        emailEditText = view.findViewById(R.id.email)
        passwordEditText = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.login)
        registerButton = view.findViewById(R.id.regButton)
        loginButton!!.setOnClickListener(View.OnClickListener {
            val email = emailEditText!!.getText().toString()
            val password = passwordEditText!!.getText().toString()
            if (email.length > 0 && password.length > 0) {
                loginRegisterViewModel!!.login(email, password)
            } else {
                Toast.makeText(
                    context,
                    "Email Address and Password Must Be Entered",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        registerButton!!.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_loginRegisterFragment_to_registerUserFragment)
        }
        return view
    }
}