package com.example.mobitail.consumer.mainActivities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.util.Base64
import android.widget.Toast
import android.widget.Button
import android.content.Intent
import android.content.Context
import android.widget.ImageView
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity

import com.example.mobitail.R
import com.example.mobitail.AESDecrypt
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.example.mobitail.databaseorganization.Secrets
import com.example.mobitail.databaseorganization.CustomerTable

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage

import javax.crypto.spec.SecretKeySpec


class SignupProfie : AppCompatActivity() {
    private lateinit var selectImageBtn: Button
    private lateinit var uploadImageBtn: Button
    private lateinit var imagePreview: ImageView
    private lateinit var imageUri: Uri
    private lateinit var storageRef: StorageReference
    private lateinit var dbRef: DatabaseReference
    private lateinit var sdbref: DatabaseReference
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_profie)

        selectImageBtn = findViewById(R.id.selectImageBtn)
        uploadImageBtn = findViewById(R.id.uploadImageBtn)
        imagePreview = findViewById(R.id.imagePreview)

        storageRef = FirebaseStorage.getInstance().getReference("userProfiles")
        sdbref = FirebaseDatabase.getInstance().getReference("secrets")
        dbRef = FirebaseDatabase.getInstance().getReference("customers")
        db = openOrCreateDatabase("mobitail", Context.MODE_PRIVATE, null)

        selectImageBtn.setOnClickListener {
            openGallery()
        }

        uploadImageBtn.setOnClickListener {
            uploadImage()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityIfNeeded(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {
        val imageLabel = "images/${System.currentTimeMillis()}_${imageUri.lastPathSegment}"
        val imageRef = storageRef.child(imageLabel)
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl

        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()
                val userEmail = intent.getStringExtra("email")
                val userPass = intent.getStringExtra("pass")

                if (userEmail != null && userPass != null) {
                    dbRef.orderByChild("email").equalTo(userEmail)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (userSnapshot in snapshot.children) {
                                        val key = userSnapshot.key
                                        val user = userSnapshot.getValue(CustomerTable::class.java)
                                        val password = user?.password

                                        login(sdbref, key, password, userPass) { isUser ->
                                            if (isUser) {
                                                userSnapshot.child("deviceName").ref.setValue(downloadUrl)
                                                Toast.makeText(
                                                    this@SignupProfie,
                                                    "Profile Created Successfully!",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                var intent = Intent(this@SignupProfie, HomeActivity::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@SignupProfie,
                                    "An error occurred while accessing the database.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }

            } else {
                Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            imagePreview.setImageURI(imageUri)
            imagePreview.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
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
                                if (decryptedPasswordString.toString() == input) {
                                    isUser = true
                                }
                            }
                        }
                    }
                    callback.invoke(isUser)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@SignupProfie,
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
}
