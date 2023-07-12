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
import com.example.mobitail.consumer.mainActivities.HomeActivity
import com.example.mobitail.consumer.modalClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
            val emailColumnIndex = cursor.getColumnIndex("email")
            val deviceNameColumnIndex = cursor.getColumnIndex("devicename")
            val deviceIdColumnIndex = cursor.getColumnIndex("deviceid")

            if (emailColumnIndex != -1 && deviceIdColumnIndex != -1 && deviceNameColumnIndex != -1) {
                val email = cursor.getString(emailColumnIndex)
                val devicename = cursor.getString(deviceNameColumnIndex)
                val deviceid = cursor.getString(deviceIdColumnIndex)

                FirebaseUtils.isUserLoggedIn(email) { isLoggedIn ->
                    if (isLoggedIn) {
                        Toast.makeText(this, "User is logged in", Toast.LENGTH_SHORT).show()
                        FirebaseUtils.isDeviceLoggedIn(devicename, deviceid, this@SplashActivity){ isLoggedIn ->
                            if (isLoggedIn) {
                                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                                startActivity(intent)

                            } else {
                                Toast.makeText(this, "Device unknown!\nplease sign in again", Toast.LENGTH_SHORT).show()
                                FirebaseUtils.logoutUser()
                                val intent = Intent(this@SplashActivity, SigninActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } else {
                        Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SplashActivity, StartUpActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(
                    this@SplashActivity,
                    "Column not found in the database",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@SplashActivity, SigninActivity::class.java)
                startActivity(intent)
            }
        } else {
            Toast.makeText(
                this@SplashActivity,
                "No data found in the database",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
        }

        cursor.close()
    }

    private fun getDb(): SQLiteDatabase {
        return SQLDatabaseManager.getDatabase(applicationContext)
    }
}