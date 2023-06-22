package com.example.mobitail.mainActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.ImageButton
import android.widget.Toast
import com.example.mobitail.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var get_user_firstname: TextInputEditText
    private lateinit var get_user_lastname: TextInputEditText
    private lateinit var get_user_email: TextInputEditText
    private lateinit var get_user_password: TextInputEditText
    private lateinit var confirm_user_password: TextInputEditText
    private lateinit var create_account: MaterialButton
    private lateinit var back_btn: ImageButton
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        FirebaseApp.initializeApp(this)

        // Get reference to the "user" node in the Firebase Realtime Database
        dbref = FirebaseDatabase.getInstance().getReference("user")

        // Initialize the views
        back_btn = findViewById(R.id.Back_btn)
        get_user_firstname = findViewById(R.id.FirstName)
        get_user_lastname = findViewById(R.id.LastName)
        get_user_email = findViewById(R.id.UserEmail)
        get_user_password = findViewById(R.id.Password)
        confirm_user_password = findViewById(R.id.ConfirmPassword)
        create_account = findViewById(R.id.CreateAccount)

        create_account.setOnClickListener {
            val fields = listOf(get_user_firstname, get_user_lastname, get_user_email, get_user_password, confirm_user_password)
            val fieldNames = listOf("FirstName", "LastName", "UserEmail", "Password", "ConfirmPassword")
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
                val userid = dbref.push().key!!

                val user = User(
                    first_name = fields[0].text.toString(),
                    last_name = fields[1].text.toString(),
                    e_mail = fields[2].text.toString(),
                    pass = fields[3].text.toString(),
                    deviceName = "",
                    deviceId = "",
                    contact = "",
                    location = ""
                )
                dbref.child(userid).setValue(user).addOnCompleteListener {
                    Toast.makeText(this, "User Registered Successfully!",
                        Toast.LENGTH_SHORT).show()
                    val register = Intent(this, SigninActivity::class.java)
                    startActivity(register)

                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)

                }.addOnFailureListener {
                        err -> Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show() }
            }
        }
        back_btn.setOnClickListener {
            var intent = Intent(this, StartUpActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
        }
    }
}

data class User(
    val first_name: String = "",
    val last_name: String = "",
    val e_mail: String = "",
    val pass: String = "",
    val deviceName: String = "",
    val deviceId: String = "",
    val contact: String = "",
    val location: String = ""
) {
    init {
        require(contact.length >= 10 || contact.isEmpty()) { "Contact should be at least 10 characters long." }
    }

    constructor() : this("", "", "", "", "")

    // Other methods and logic
}
