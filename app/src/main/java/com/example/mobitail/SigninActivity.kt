package com.example.mobitail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SigninActivity : AppCompatActivity() {
    lateinit var back: ImageButton
    lateinit var signin: MaterialButton
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)


        back = findViewById(R.id.Back_btn)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        signin = findViewById(R.id.signin)


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

                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("user")

                // Perform a query to check if the user exists in the database
                reference.orderByChild("e_mail").equalTo(userEmail)
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
                                        val intent = Intent(this@SigninActivity, HomeActivity::class.java)
                                        startActivity(intent)

                                        overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
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