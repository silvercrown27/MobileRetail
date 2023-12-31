package com.example.mobitail.consumer.mainActivities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobitail.AESEncrypt
import com.example.mobitail.R
import com.example.mobitail.SQLDatabaseManager
import com.example.mobitail.StartUpActivity
import com.example.mobitail.databaseorganization.CustomerTable
import com.example.mobitail.databaseorganization.RetailTable
import com.example.mobitail.databaseorganization.Secrets
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.SecretKey

class SignUpActivity : AppCompatActivity() {
    private lateinit var get_user_firstname: TextInputEditText
    private lateinit var get_user_lastname: TextInputEditText
    private lateinit var get_user_email: TextInputEditText
    private lateinit var get_user_password: TextInputEditText
    private lateinit var confirm_user_password: TextInputEditText
    private lateinit var create_account: MaterialButton
    private lateinit var back_btn: ImageButton
    private lateinit var userdbref: DatabaseReference
    private lateinit var secretsdbref: DatabaseReference
    private lateinit var db: SQLiteDatabase

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        FirebaseApp.initializeApp(this)

        userdbref = FirebaseDatabase.getInstance().getReference("customers")
        secretsdbref = FirebaseDatabase.getInstance().getReference("secrets")
        db = getDb()

        back_btn = findViewById(R.id.Back_btn)
        get_user_firstname = findViewById(R.id.FirstName)
        get_user_lastname = findViewById(R.id.LastName)
        get_user_email = findViewById(R.id.UserEmail)
        get_user_password = findViewById(R.id.Password)
        confirm_user_password = findViewById(R.id.ConfirmPassword)
        create_account = findViewById(R.id.CreateAccount)

        create_account.setOnClickListener {
            val fields = listOf(get_user_firstname, get_user_lastname, get_user_email, get_user_password, confirm_user_password)
            val fieldNames = listOf("First Name", "Last Name", "Email", "Password", "Confirm Password")
            var hasError = false
            val auth = FirebaseAuth.getInstance()

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
                get_user_password.error = "Passwords do not match!"
                confirm_user_password.error = "Passwords do not match!"
                hasError = true
            }

            if (!hasError) {
                val password = get_user_password.text.toString()
                val encryptor = AESEncrypt()
                val encryptedData = encryptor.encrypt(password)
                val encryptedPassword = Base64.encodeToString(encryptedData, Base64.DEFAULT)

                val currentDate = Date()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val dateString = dateFormat.format(currentDate)
                val timeString = timeFormat.format(currentDate)
                val dateTimeString = "$dateString - $timeString"
                val user = CustomerTable(
                    firstname = fields[0].text.toString(),
                    lastname = fields[1].text.toString(),
                    email = fields[2].text.toString(),
                    password = encryptedPassword,
                    location = TimeZone.getDefault().id,
                    doc = dateTimeString,
                    deviceName = Build.MODEL,
                    deviceId = Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    ),
                    docTimeZone = TimeZone.getDefault().id
                )

                val secretKey: SecretKey? = encryptor.mySecretKey
                val initializationVector: ByteArray? = encryptor.myInitializationVector

                val encodedKey = secretKey?.encoded?.map { it.toInt() }
                val encodedInitVector = initializationVector?.map { it.toInt() }

                val key = Secrets(
                    key = encodedKey,
                    initV = encodedInitVector
                )

                createUser(user.email, password, user, key)

            } else {
                Toast.makeText(this, "Encountered an error during sign up\nPlease try again after sometime", Toast.LENGTH_SHORT).show()
            }
        }

        back_btn.setOnClickListener {
            var intent = Intent(this, StartUpActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
        }
    }

    private fun getDb(): SQLiteDatabase {
        return SQLDatabaseManager.getDatabase(applicationContext)
    }

    private fun createUser(email: String, password: String, users: CustomerTable, key: Secrets) {
        val auth = FirebaseAuth.getInstance()

        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { createTask ->
                                if (createTask.isSuccessful) {
                                    val userId = auth.currentUser?.uid

                                    addRecords(userId, users, key)
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

    private fun  addRecords(userId: String?, users: CustomerTable, key: Secrets){
        secretsdbref.child(userId.toString()).setValue(key)
            .addOnCompleteListener {
                Toast.makeText(this, "secrets created", Toast.LENGTH_LONG)
                    .show()

                userdbref.child(userId.toString()).setValue(users)
                    .addOnCompleteListener {
                        Toast.makeText(
                            this,
                            "User Registered Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val firstName = users.firstname
                        val lastName = users.lastname
                        val userEmail = users.email
                        val deviceName = Build.MODEL
                        val deviceId = Settings.Secure.getString(
                            contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                        val contact = ""
                        val location = ""
                        val gender = ""

                        val insertQuery =
                            "INSERT INTO users (firstname, lastname, email, devicename, deviceid, contact, location, gender) " +
                                    "VALUES ('$firstName', '$lastName', '$userEmail', '$deviceName', '$deviceId', '$contact', '$location', '$gender')"

                        db.execSQL(insertQuery)
                        db.close()

                        val intent = Intent(this, SignupProfie::class.java)
                        intent.putExtra("email", userEmail)
                        intent.putExtra(
                            "pass",
                            get_user_password.text.toString()
                        )
                        startActivity(intent)

                        overridePendingTransition(
                            R.anim.fade_animation,
                            R.anim.fade_out
                        )
                    }.addOnFailureListener { err ->
                        Toast.makeText(
                            this,
                            "Error ${err.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "secrets failed, please try again later",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}