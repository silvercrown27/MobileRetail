package com.example.mobitail.consumer.mainActivities

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.mobitail.R
import com.example.mobitail.SigninActivity

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

            val db = getDb()

            val selectQuery = "SELECT * FROM users WHERE active = 0"
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                val firstNameColumnIndex = cursor.getColumnIndex("firstname")
                val emailColumnIndex = cursor.getColumnIndex("email")

                if (firstNameColumnIndex != -1 && emailColumnIndex != -1) {
                    do {
                        val firstName = cursor.getString(firstNameColumnIndex)
                        val email = cursor.getString(emailColumnIndex)

                        Toast.makeText(
                            this@SettingsActivity,
                            "$firstName has been deleted!",
                            Toast.LENGTH_LONG
                        ).show()

                        val deleteQuery = "DELETE FROM users WHERE email = '$email'"
                        db.execSQL(deleteQuery)
                    } while (cursor.moveToNext())
                } else {
                    Toast.makeText(
                        this@SettingsActivity,
                        "Column not found in the database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@SettingsActivity,
                    "No data found in the database",
                    Toast.LENGTH_SHORT
                ).show()
            }

            cursor.close()

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
