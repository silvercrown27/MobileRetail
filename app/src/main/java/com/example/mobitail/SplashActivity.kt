package com.example.mobitail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.database.sqlite.SQLiteDatabase
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.Toast
import com.example.mobitail.Consumer.mainActivities.HomeActivity
import com.example.mobitail.Consumer.modalClasses.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class SplashActivity : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var database: SQLiteDatabase
    private val SPLASH_DURATION: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        dbref = FirebaseDatabase.getInstance().getReference("user")

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.startOffset = 1000
        fadeOut.duration = 1000

        val splashImage = findViewById<ImageView>(R.id.splash_image)

        splashImage.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            splashImage.startAnimation(fadeOut)
        }, 1000)

        val db = getDb()

        val selectQuery = "SELECT * FROM users"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            val firstNameColumnIndex = cursor.getColumnIndex("firstname")
            val lastNameColumnIndex = cursor.getColumnIndex("lastname")
            val emailColumnIndex = cursor.getColumnIndex("email")
            val activeColumnIndex = cursor.getColumnIndex("active")
            val deviceNameColumnIndex = cursor.getColumnIndex("devicename")
            val deviceIdColumnIndex = cursor.getColumnIndex("deviceid")

            // Check if the column exists
            if (firstNameColumnIndex != -1 && lastNameColumnIndex != -1 && emailColumnIndex != -1 &&
                activeColumnIndex != -1) {
                val firstName = cursor.getString(firstNameColumnIndex)
                val lastName = cursor.getString(lastNameColumnIndex)
                val email = cursor.getString(emailColumnIndex)
                val active = cursor.getInt(activeColumnIndex)
                val devicename = cursor.getString(deviceNameColumnIndex)
                val deviceid = cursor.getString(deviceIdColumnIndex)

                Toast.makeText(
                    this@SplashActivity,
                    "User Information:\nFirst Name: $firstName\nLast Name: $lastName\nEmail: $email",
                    Toast.LENGTH_LONG
                ).show()

                if (active == 0) {
                    if (check_database(email, devicename, deviceid)){
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, SPLASH_DURATION)
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@SplashActivity, StartUpActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, SPLASH_DURATION)
                }
            } else {
                Toast.makeText(
                    this@SplashActivity,
                    "Column not found in the database",
                    Toast.LENGTH_SHORT
                ).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SplashActivity, SigninActivity::class.java)
                    startActivity(intent)
                    finish()
                }, SPLASH_DURATION)
            }
        } else {
            Toast.makeText(
                this@SplashActivity,
                "No data found in the database",
                Toast.LENGTH_SHORT
            ).show()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_DURATION)
        }

        cursor.close()
    }
    private fun getDb(): SQLiteDatabase {
        if (!::database.isInitialized) {
            database = openOrCreateDatabase("mobitail", Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS users(" +
                    "firstname VARCHAR," +
                    "lastname VARCHAR," +
                    "email VARCHAR," +
                    "devicename VARCHAR," +
                    "deviceid VARCHAR," +
                    "contact VARCHAR," +
                    "location VARCHAR)")
        }
        return database
    }
    private fun check_database(email: String, devicename: String, deviceID: String): Boolean {
        var userExists = true

        dbref.orderByChild("e_mail").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            if (user?.deviceId == deviceID && user?.deviceName == devicename) {
                                Toast.makeText(
                                    this@SplashActivity,
                                    "SUCCESS!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                userExists = false
                                Toast.makeText(
                                    this@SplashActivity,
                                    "FAIL!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return
                        }
                    } else {
                        userExists = false
                        Toast.makeText(
                            this@SplashActivity,
                            "User does not wxist!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@SplashActivity,
                        "An error occurred while accessing the database.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        return userExists
    }
}