package com.example.mobitail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobitail.consumer.mainActivities.HomeActivity
import com.example.mobitail.databaseorganization.CustomerTable

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


object FirebaseUtils {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val customersRef = database.getReference("customers")
    private val retailRef = database.getReference("retailers")

    fun getUserData(callback: (CustomerTable?) -> Unit) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userRef: DatabaseReference = customersRef.child(userId)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(CustomerTable::class.java)
                    callback.invoke(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.invoke(null)
                }
            })
        } else {
            callback.invoke(null)
        }
    }

    fun prepareData(activity: AppCompatActivity, callback: (CustomerTable) -> Unit) {
        getUserData { user ->
            if (user != null) {
                callback.invoke(user)
            } else {
                Toast.makeText(activity, "User data is not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isUserLoggedIn(email: String, callback: (Boolean) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val emailRef = customersRef.orderByChild("email").equalTo(email)

            emailRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val loggedIn = snapshot.children.any { it.key == userId }
                    callback.invoke(loggedIn)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.invoke(false)
                }
            })
        } else {
            callback.invoke(false)
        }
    }

    fun isDeviceLoggedIn(
        deviceName: String,
        deviceId: String,
        context: Context,
        callback: (Boolean) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        customersRef.orderByKey().equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("HardwareIds")
                override fun onDataChange(snapshot: DataSnapshot) {
                    var isLoggedIn = false

                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(CustomerTable::class.java)
                            val devicenm = user?.deviceName
                            val deviceid = user?.deviceId

                            if (deviceName == devicenm && deviceId == deviceid) {
                                isLoggedIn = true
                            }
                        }
                    }

                    callback.invoke(isLoggedIn)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        context,
                        "An error occurred while accessing the database.",
                        Toast.LENGTH_SHORT
                    ).show()

                    callback.invoke(false)
                }
            })
    }


    fun logoutUser() {
        auth.signOut()
    }
}
