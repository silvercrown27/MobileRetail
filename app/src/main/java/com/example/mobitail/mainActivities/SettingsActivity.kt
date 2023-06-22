package com.example.mobitail.mainActivities

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.mobitail.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var back_btn: ImageButton
    private lateinit var logout_btn: Button
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        back_btn = findViewById(R.id.Back_btn)
        logout_btn = findViewById(R.id.Logout_btn)

        back_btn.setOnClickListener {
            var intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
        }

        logout_btn.setOnClickListener {

            val db = getDb() // Initialize the db variable before using it

            // Rest of the code for reading information from the database
            val selectQuery = "SELECT * FROM users"
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                val firstNameColumnIndex = cursor.getColumnIndex("firstname")
                val lastNameColumnIndex = cursor.getColumnIndex("lastname")
                val emailColumnIndex = cursor.getColumnIndex("email")
                val deviceNameColumnIndex = cursor.getColumnIndex("devicename")
                val deviceIdColumnIndex = cursor.getColumnIndex("deviceid")
                val contactColumnIndex = cursor.getColumnIndex("contact")
                val locationColumnIndex = cursor.getColumnIndex("location")

                // Check if the column exists
                if (firstNameColumnIndex != -1 && lastNameColumnIndex != -1 && emailColumnIndex != -1 &&
                    deviceNameColumnIndex != -1 && deviceIdColumnIndex != -1 && contactColumnIndex != -1 &&
                    locationColumnIndex != -1
                ) {
                    val firstName = cursor.getString(firstNameColumnIndex)
                    val lastName = cursor.getString(lastNameColumnIndex)
                    val email = cursor.getString(emailColumnIndex)
                    val deviceName = cursor.getString(deviceNameColumnIndex)
                    val deviceId = cursor.getString(deviceIdColumnIndex)
                    val contact = cursor.getString(contactColumnIndex)
                    val location = cursor.getString(locationColumnIndex)

                    // Display the retrieved information
                    Toast.makeText(
                        this@SettingsActivity,
                        "User Information:\nFirst Name: $firstName\nLast Name: $lastName\nEmail: $email\nDevice Name: $deviceName\nDevice ID: $deviceId\nContact: $contact\nLocation: $location",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    // Handle column not found error
                    Toast.makeText(
                        this@SettingsActivity,
                        "Column not found in the database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Handle empty cursor or no rows error
                Toast.makeText(
                    this@SettingsActivity,
                    "No data found in the database",
                    Toast.LENGTH_SHORT
                ).show()
            }

            cursor.close()

            // Redirect to the SigninActivity
            val intent = Intent(this@SettingsActivity, SigninActivity::class.java)
            startActivity(intent)
        }
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
