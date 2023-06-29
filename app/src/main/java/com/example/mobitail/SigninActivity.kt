package com.example.mobitail

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobitail.Consumer.mainActivities.HomeActivity
import com.example.mobitail.Consumer.modalClasses.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SigninActivity : AppCompatActivity() {
    private lateinit var back: ImageButton
    private lateinit var signin: MaterialButton
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var dbref: DatabaseReference
    lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        db = openOrCreateDatabase("mobitail", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS users(" +
                "firstname VARCHAR," +
                "lastname VARCHAR," +
                "email VARCHAR," +
                "devicename VARCHAR," +
                "deviceid VARCHAR," +
                "contact VARCHAR," +
                "location VARCHAR," +
                "active INTEGER)")

        back = findViewById(R.id.Back_btn)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        signin = findViewById(R.id.signin)

        dbref = FirebaseDatabase.getInstance().getReference("user")

        signin.setOnClickListener {
            val fields = listOf(email, password)
            val fieldNames = listOf("email", "password")
            var hasError = false

            for (i in fields.indices) {
                val field = fields[i]
                val fieldName = fieldNames[i]
                val fieldValue = field.text.toString().trim()

                if (fieldValue.isEmpty()) {
                    Toast.makeText(this, "Please Enter All Your Details", Toast.LENGTH_SHORT).show()
                    field.error = "Please fill in the $fieldName field!"
                    field.requestFocus()
                    hasError = true
                }
            }

            if (!hasError) {
                val userEmail = email.text.toString().trim()
                val userPassword = password.text.toString().trim()

                // Perform a query to check if the user exists in the database
                dbref.orderByChild("e_mail").equalTo(userEmail)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (userSnapshot in snapshot.children) {
                                    val user = userSnapshot.getValue(User::class.java)
                                    if (user?.pass == userPassword) {
                                        Toast.makeText(
                                            this@SigninActivity,
                                            "LOGIN SUCCESS!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        userSnapshot.child("deviceName").ref.setValue(Build.MODEL)
                                        userSnapshot.child("deviceId").ref.setValue(
                                            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                                        ).addOnCompleteListener {
                                                Toast.makeText(
                                                    this@SigninActivity,
                                                    "User Registered Successfully!",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                val firstName = user?.first_name ?: ""
                                                val lastName = user?.last_name ?: ""
                                                val deviceName = user?.deviceName ?: ""
                                                val deviceId = user?.deviceId ?: ""
                                                val contact = user?.contact ?: ""
                                                val location = user?.location ?: ""
                                                val active = 0

                                                val insertQuery = "INSERT INTO users (firstname, lastname, email, devicename, deviceid, contact, location, active) " +
                                                        "VALUES ('$firstName', '$lastName', '$userEmail', '$deviceName', '$deviceId', '$contact', '$location', '$active')"
                                                db.execSQL(insertQuery)

                                                val intent = Intent(this@SigninActivity, HomeActivity::class.java)
                                                startActivity(intent)
                                                overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                                            }
                                            .addOnFailureListener { err ->
                                                Toast.makeText(this@SigninActivity, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    } else {
                                        Toast.makeText(
                                            this@SigninActivity,
                                            "Sorry, your details are incorrect!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    return
                                }
                            } else {
                                Toast.makeText(
                                    this@SigninActivity,
                                    "Sorry, your details are incorrect!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@SigninActivity,
                                "An error occurred while accessing the database.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

            }
        }

        back.setOnClickListener {
            var intent = Intent(this, StartUpActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
        }
    }
}