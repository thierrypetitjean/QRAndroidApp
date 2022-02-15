package com.example.qrregistration.view

import android.widget.EditText
import com.example.qrregistration.viewmodel.LoginRegisterViewModel
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import androidx.navigation.Navigation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.qrregistration.R
import com.example.qrregistration.viewmodel.RegisterUserViewModel

class RegisterUserFragment : Fragment() {
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var usernameEditText: EditText? = null
    private var registerButton: Button? = null
    private var registerUserViewModel: RegisterUserViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerUserViewModel = ViewModelProvider(this).get(
            RegisterUserViewModel::class.java
        )
        registerUserViewModel!!.userLiveData.observe(this) { firebaseUser ->
            if (firebaseUser != null) {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_registerUserFragment_to_RegisterUserFragment)

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_user, container, false)
        emailEditText = view.findViewById(R.id.email)
        passwordEditText = view.findViewById(R.id.password)
        registerButton = view.findViewById(R.id.regButton)
        usernameEditText = view.findViewById(R.id.username)

        registerButton!!.setOnClickListener{
            val email = emailEditText!!.getText().toString()
            val password = passwordEditText!!.getText().toString()
            val username = usernameEditText!!.getText().toString()
            if (email.length > 0 && password.length > 0 && username.length > 3) {
                registerUserViewModel!!.register(email, password, username)
            } else {
                Toast.makeText(
                    context,
                    "Email Address, Password and Username Must Be Entered",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view
    }
}