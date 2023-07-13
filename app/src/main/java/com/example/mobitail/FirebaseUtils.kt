package com.example.mobitail

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.mobitail.databaseorganization.CustomerTable

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
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

    fun isUserLoggedIn(table: String, callback: (Boolean) -> Unit) {
        val currentUser = auth.currentUser!!.uid

        val usersRef: Query = when (table) {
            "retailers" -> retailRef.orderByKey()
            "customers" -> customersRef.orderByKey()
            else -> {
                callback.invoke(false)
                return
            }
        }

        val query = usersRef.equalTo(currentUser)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loggedIn = snapshot.children.any { it.key == currentUser }
                callback.invoke(loggedIn)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.invoke(false)
            }
        })
    }


    fun logoutUser() {
        auth.signOut()
    }

}
