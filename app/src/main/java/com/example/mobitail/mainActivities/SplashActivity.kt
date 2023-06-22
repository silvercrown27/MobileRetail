package com.example.mobitail.mainActivities

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobitail.R


class SplashActivity : AppCompatActivity() {
    private lateinit var database: SQLiteDatabase
    private val SPLASH_DURATION: Long = 2000

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
            val firstNameColumnIndex = cursor.getColumnIndex("firstname")
            val lastNameColumnIndex = cursor.getColumnIndex("lastname")
            val emailColumnIndex = cursor.getColumnIndex("email")
            val activeColumnIndex = cursor.getColumnIndex("active")

            // Check if the column exists
            if (firstNameColumnIndex != -1 && lastNameColumnIndex != -1 && emailColumnIndex != -1 &&
                activeColumnIndex != -1) {
                val firstName = cursor.getString(firstNameColumnIndex)
                val lastName = cursor.getString(lastNameColumnIndex)
                val email = cursor.getString(emailColumnIndex)
                val active = cursor.getInt(activeColumnIndex)

                Toast.makeText(
                    this@SplashActivity,
                    "User Information:\nFirst Name: $firstName\nLast Name: $lastName\nEmail: $email",
                    Toast.LENGTH_LONG
                ).show()

                if (active == 0) {
                    // Proceed to the main activity after the splash screen duration
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, SPLASH_DURATION)
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
}
