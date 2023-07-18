package com.example.mobitail.retailer.mainActivities

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.widget.ImageButton
import android.widget.Toast

import com.example.mobitail.AESEncrypt
import com.example.mobitail.R
import com.example.mobitail.SQLDatabaseManager
import com.example.mobitail.databaseorganization.CustomerTable
import com.example.mobitail.databaseorganization.RetailTable
import com.example.mobitail.databaseorganization.Secrets

import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.SecretKey


class retailSignupActivity : AppCompatActivity() {
    private lateinit var get_user_firstname: TextInputEditText
    private lateinit var get_user_lastname: TextInputEditText
    private lateinit var get_user_email: TextInputEditText
    private lateinit var get_user_password: TextInputEditText
    private lateinit var confirm_user_password: TextInputEditText
    private lateinit var next: MaterialButton
    private lateinit var back_btn: ImageButton
    private lateinit var next_step: MaterialButton
    private lateinit var dbref: DatabaseReference
    private lateinit var retdb: SQLiteDatabase

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailsignup)

        dbref = FirebaseDatabase.getInstance().getReference("Retailer")

        next_step = findViewById(R.id.validate_step1)

        back_btn = findViewById(R.id.Back_btn)
        get_user_firstname = findViewById(R.id.FirstName)
        get_user_lastname = findViewById(R.id.LastName)
        get_user_email = findViewById(R.id.UserEmail)
        get_user_password = findViewById(R.id.Password)
        confirm_user_password = findViewById(R.id.ConfirmPassword)
        next = findViewById(R.id.validate_step1)

        next_step.setOnClickListener {
            val fields = listOf(
                get_user_firstname,
                get_user_lastname,
                get_user_email,
                get_user_password,
                confirm_user_password
            )
            val fieldNames =
                listOf("FirstName", "LastName", "UserEmail", "Password", "ConfirmPassword")
            var hasError = false

            for (i in fields.indices) {
                val field = fields[i]
                val fieldName = fieldNames[i]
                val fieldValue = field.text.toString().trim()

                if (fieldValue.isEmpty()) {
                    field.error = "Please fill in the $fieldName field!"
                    field.requestFocus()
                    hasError = true
                }
            }

            if (get_user_password.text.toString() != confirm_user_password.text.toString()) {
                get_user_password.error = "Passwords does not match!"
                confirm_user_password.error = "Passwords does not match!"
                hasError = true
            }

            if (!hasError) {
                val password = fields[3].text.toString()
                val currentDate = Date()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val dateString = dateFormat.format(currentDate)
                val timeString = timeFormat.format(currentDate)
                val dateTimeString = "$dateString - $timeString"

                val user = RetailTable(
                    firstname = fields[0].text.toString(),
                    lastname = fields[1].text.toString(),
                    email = fields[2].text.toString(),
                    contact = "",
                    doc = dateTimeString,
                    location = TimeZone.getDefault().id,
                    gender = "",
                    deviceName = Build.MODEL,
                    deviceId = Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    ),
                    docTimeZone = TimeZone.getDefault().id
                )

                createUser(user.email, password, user)

            } else {
                Toast.makeText(this, "Encountered an error during sign up\nPlease try again after sometime", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser(email: String, password: String, users: RetailTable) {
        val auth = FirebaseAuth.getInstance()

        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        // User does not exist, proceed with user creation
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { createTask ->
                                if (createTask.isSuccessful) {
                                    val userId = auth.currentUser?.uid

                                    addRecords(userId, users)
                                } else {
                                    Toast.makeText(this, "Failed to create user1\nPlease try again later", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "The account already exists\nproceed to login", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error occurred while checking user existence!", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun addRecords(userId: String?, users: RetailTable) {
        val userdbref: DatabaseReference = FirebaseDatabase.getInstance().getReference("retailers")

        userdbref.child(userId.toString()).setValue(users)
            .addOnCompleteListener {
                Toast.makeText(
                    this,
                    "User Registered Successfully!",
                    Toast.LENGTH_SHORT
                ).show()

                enterSQLData(users)

                var intent = Intent(this, SignUpStep2::class.java)
                intent.putExtra("userid", userId)
                startActivity(intent)

                overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)

            }.addOnFailureListener { err ->
                Toast.makeText(
                    this,
                    "Error ${err.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getDb(): SQLiteDatabase {
        return SQLDatabaseManager.getDatabase(applicationContext)
    }

    private fun enterSQLData(user: Any) {
        val db = getDb()
        val fields = when (user) {
            is RetailTable -> listOf(
                "firstname", "lastname", "email", "contact", "location", "gender"
            )
            is CustomerTable -> listOf(
                "firstname", "lastname", "email", "contact", "location", "gender"
            )
            else -> throw IllegalArgumentException("Invalid data class type")
        }

        val deviceName = Build.MODEL
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        val fieldValues = fields.joinToString(", ") { field ->
            val declaredField = user::class.java.getDeclaredField(field)
            declaredField.isAccessible = true
            val fieldValue = declaredField.get(user)
            "'$fieldValue'"
        }

        val insertQuery = "INSERT OR REPLACE INTO users (devicename, deviceid, ${fields.joinToString(", ")}) " +
                "VALUES ('$deviceName', '$deviceId', $fieldValues)"
        db.execSQL(insertQuery)
        db.close()
    }

}
