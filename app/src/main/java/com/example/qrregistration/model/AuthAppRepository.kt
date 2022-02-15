package com.example.qrregistration.model

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AuthAppRepository(private val application: Application) {

    private val firebaseAuth: FirebaseAuth
    private val firebaseDatabase: FirebaseDatabase
    val userLiveData: MutableLiveData<FirebaseUser?>
    val alreadyLoggedInToday: MutableLiveData<Boolean>
    val loggedOutLiveData: MutableLiveData<Boolean>

    var currentDate: String? = null
    var currentTime: String? = null

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userLiveData = MutableLiveData()
        alreadyLoggedInToday = MutableLiveData()
        loggedOutLiveData = MutableLiveData()
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
    }

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)
                }
                else
                {
                    Toast.makeText(
                        application.applicationContext,
                        "Login Failure: " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun register(email: String, password: String, username: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(username).build()

                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("test", "User profile updated.")
                                userLiveData.postValue(firebaseAuth.currentUser)
                            }
                        }
                } else {
                    Toast.makeText(
                        application.applicationContext,
                        "Registration Failure: " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    fun registerToday(username: String, email: String, date: String, time: String) {

        val key = firebaseDatabase.getReference().child("aanmeldingen").push().key
        if (key == null) {
            Log.w("test", "Couldn't get push key for aanmeldingen")
            return
        }

        val register = Register(username, email, date, time)
        val postValues = register.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/aanmeldingen-" + date + "/$username" to postValues
        )

        firebaseDatabase.getReference().updateChildren(childUpdates)
            .addOnSuccessListener {
                // Write was successful!
                Log.d("test","write succesfull ")
                currentDate = date
                currentTime = time

            }
            .addOnFailureListener {
                // Write failed
                Log.d("test","error = " + it.localizedMessage)
            }

    }

    fun checkToday(date: String)
    {
        // Read from the database
        val myMostViewedPostsQuery = firebaseDatabase.getReference().child("aanmeldingen-" + date).child(
            firebaseAuth.currentUser!!.displayName!!)
        myMostViewedPostsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                val dateToday = dataSnapshot.child("register_date").value
                val time = dataSnapshot.child("register_time").value
                if(dateToday != null && time != null) {
                    Log.d("test", "Already registered today : $dateToday " + " $time")
                    currentDate = date
                    currentTime = time.toString()
                    alreadyLoggedInToday.postValue(true)
                }else
                {
                    currentDate = ""
                    currentTime = ""
                    Log.d("test", "Not registered today")
                    alreadyLoggedInToday.postValue(null)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // User not registered today
                Log.d("test", "not registered today")

            }
        })
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

}