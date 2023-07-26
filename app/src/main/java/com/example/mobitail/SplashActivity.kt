package com.example.mobitail

import android.app.NotificationManager
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
import com.example.mobitail.retailer.mainActivities.RetailDashboardActivity
import com.google.firebase.auth.FirebaseAuth


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

        getSystemService(NotificationManager::class.java)


        splashImage.startAnimation(fadeIn)

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

                checkDb(email, devicename, deviceid)
                cursor.close()
            } else {
                Toast.makeText(
                    this@SplashActivity,
                    "Logged Out",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@SplashActivity, SigninActivity::class.java)
                startActivity(intent)
                cursor.close()
            }
        } else {
            Toast.makeText(
                this@SplashActivity,
                "Logged Out",
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

    private fun checkDb(email: String, devicename: String, deviceid: String){
        FirebaseUtils.isUserLoggedIn("retailers") { isLoggedIn ->
            if (isLoggedIn) {
                val intent = Intent(this@SplashActivity, RetailDashboardActivity::class.java)
                startActivity(intent)

            } else {
                FirebaseUtils.isUserLoggedIn("customers") { isLoggedIn ->
                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                    startActivity(intent)

                    if (isLoggedIn) {
                        Toast.makeText(this, "User is logged in", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@SplashActivity, StartUpActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
        overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
    }
}