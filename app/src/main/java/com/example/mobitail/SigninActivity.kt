package com.example.mobitail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import android.content.Intent
import android.content.Context
import android.provider.Settings
import android.widget.ImageButton
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity

import com.example.mobitail.consumer.mainActivities.HomeActivity
import com.example.mobitail.databaseorganization.CustomerTable
import com.example.mobitail.databaseorganization.Secrets

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

import javax.crypto.spec.SecretKeySpec

class SigninActivity : AppCompatActivity() {
    private lateinit var back: ImageButton
    private lateinit var signin: MaterialButton
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var dbref: DatabaseReference
    private lateinit var sdbref: DatabaseReference
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        sdbref = FirebaseDatabase.getInstance().getReference("secrets")
        dbref = FirebaseDatabase.getInstance().getReference("customers")
        db = getDb()

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

                loginUser(userEmail, userPassword)
            }
        }

        back.setOnClickListener {
            var intent = Intent(this, StartUpActivity::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
        }
    }

    private fun getDb(): SQLiteDatabase {
        return SQLDatabaseManager.getDatabase(applicationContext)
    }

    private fun login(sdbref: DatabaseReference, userid: String?, password: String?, input: String, callback: (Boolean) -> Unit) {
        sdbref.orderByKey().equalTo(userid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var isUser = false
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val data = userSnapshot.getValue(Secrets::class.java)
                            if (data != null) {
                                val keyBytes = data.key?.let { convertIntArrayToByteArray(it.toIntArray()) }
                                val initVector = data.initV?.let { convertIntArrayToByteArray(it.toIntArray()) }

                                val secretKey = SecretKeySpec(keyBytes, "AES")
                                val aesDecrypt = AESDecrypt(secretKey, initVector)

                                val decryptedPassword = aesDecrypt.decrypt(
                                    Base64.decode(
                                        password,
                                        Base64.DEFAULT
                                    )
                                )
                                val decryptedPasswordString = decryptedPassword?.let {
                                    String(it, Charsets.UTF_8)
                                }
                                Toast.makeText(this@SigninActivity, decryptedPasswordString, Toast.LENGTH_SHORT).show()
                                if (decryptedPasswordString.toString() == input) {
                                    isUser = true
                                    Toast.makeText(this@SigninActivity, input, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    callback.invoke(isUser)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@SigninActivity,
                        "An error occurred while accessing the database.",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback.invoke(false)
                }
            })
    }

    private fun convertIntArrayToByteArray(intArray: IntArray): ByteArray {
        val byteArray = ByteArray(intArray.size)
        for (i in intArray.indices) {
            byteArray[i] = intArray[i].toByte()
        }
        return byteArray
    }

    private fun loginUser(userEmail: String, userPassword: String){
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    updateAccount(userId, userPassword)

                } else {
                    Toast.makeText(this@SigninActivity, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateAccount(userId: String?, input: String){
        dbref.orderByKey().equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("HardwareIds")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val key = userSnapshot.key
                            val user =
                                userSnapshot.getValue(CustomerTable::class.java)
                            val password = user?.password
                            login(
                                sdbref,
                                key,
                                password,
                                input
                            ) { isUser ->
                                if (isUser) {
                                    Toast.makeText(
                                        this@SigninActivity,
                                        "LOGIN SUCCESS!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    userSnapshot.child("deviceName").ref.setValue(
                                        Build.MODEL
                                    )
                                    userSnapshot.child("deviceId").ref.setValue(
                                        Settings.Secure.getString(
                                            contentResolver,
                                            Settings.Secure.ANDROID_ID
                                        )
                                    ).addOnCompleteListener {
                                        Toast.makeText(
                                            this@SigninActivity,
                                            "User Registered Successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        val firstName = user?.firstname
                                        val lastName = user?.lastname
                                        val email = user?.email
                                        val deviceName = Build.MODEL
                                        val deviceId =
                                            Settings.Secure.getString(
                                                contentResolver,
                                                Settings.Secure.ANDROID_ID
                                            )
                                        val contact = user?.contact
                                        val location = user?.location
                                        val gender = user?.gender

                                        val insertQuery = "INSERT INTO users (firstname, lastname, email, devicename, deviceid, contact, location, gender) " +
                                                "VALUES ('$firstName', '$lastName', '$email', '$deviceName', '$deviceId', '$contact', '$location', '$gender')"
                                        db.execSQL(insertQuery)
                                        db.close()

                                        val intent = Intent(
                                            this@SigninActivity,
                                            HomeActivity::class.java
                                        )
                                        startActivity(intent)

                                        overridePendingTransition(
                                            R.anim.fade_animation,
                                            R.anim.fade_out
                                        )
                                    }
                                        .addOnFailureListener { err ->
                                            Toast.makeText(
                                                this@SigninActivity,
                                                "Error ${err.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                } else {
                                    Toast.makeText(
                                        this@SigninActivity,
                                        "Sorry, your password is incorrect!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@SigninActivity,
                            "Sorry, Account does not exist!",
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