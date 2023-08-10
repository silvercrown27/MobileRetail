package com.example.mobitail.retailer.mainActivities

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.mobitail.FirebaseUtils
import com.example.mobitail.R
import com.example.mobitail.SQLDatabaseManager
import com.example.mobitail.SigninActivity
import com.example.mobitail.retailer.adapterClasses.SettingsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class RetailSettingsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var logout_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retail_settings)

        logout_btn = findViewById(R.id.Logout_btn)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.action_settings
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_accounts -> {
                    val intent = Intent(this, RetailAccountsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                R.id.action_dashboard -> {
                    val intent = Intent(this, RetailDashboardActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                R.id.action_products -> {
                    val intent = Intent(this, ProductsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                R.id.action_settings -> {
                    val intent = Intent(this, RetailSettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }

                else -> false
            }
        }

        logout_btn.setOnClickListener {

            FirebaseUtils.logoutUser()

            val db = getDb()

            val selectQuery = "SELECT * FROM users"
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                val firstNameColumnIndex = cursor.getColumnIndex("firstname")
                val emailColumnIndex = cursor.getColumnIndex("email")

                if (firstNameColumnIndex != -1 && emailColumnIndex != -1) {
                    do {
                        val firstName = cursor.getString(firstNameColumnIndex)
                        val email = cursor.getString(emailColumnIndex)

                        Toast.makeText(
                            this@RetailSettingsActivity,
                            "$firstName has been deleted!",
                            Toast.LENGTH_LONG
                        ).show()

                        val deleteQuery = "DELETE FROM users WHERE email = '$email'"
                        db.execSQL(deleteQuery)
                    } while (cursor.moveToNext())
                } else {
                    Toast.makeText(
                        this@RetailSettingsActivity,
                        "Column not found in the database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@RetailSettingsActivity,
                    "No data found in the database",
                    Toast.LENGTH_SHORT
                ).show()
            }

            cursor.close()

            val intent = Intent(this@RetailSettingsActivity, SigninActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getDb(): SQLiteDatabase {
        return SQLDatabaseManager.getDatabase(applicationContext)
    }

}