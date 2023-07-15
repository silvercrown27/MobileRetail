package com.example.mobitail.retailer.mainActivities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Products

import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import java.util.Date
import java.text.SimpleDateFormat


class AddRemoveProduct : AppCompatActivity() {
    private lateinit var imagePreview: ImageView
    private lateinit var addProd: MaterialButton
    private lateinit var selectProdImg: MaterialButton
    private lateinit var imageUri: Uri
    private lateinit var storageRef: StorageReference
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remove_product)

        selectProdImg = findViewById(R.id.selectImageBtn)
        addProd = findViewById(R.id.uploadProduct)
        imagePreview = findViewById(R.id.imagePreview)

        storageRef = FirebaseStorage.getInstance().getReference("products")
        dbRef = FirebaseDatabase.getInstance().getReference("products")

        selectProdImg.setOnClickListener {
            openGallery()
        }

        addProd.setOnClickListener {
            uploadProd()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityIfNeeded(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadProd() {
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val imageLabel = "$currentUser/${System.currentTimeMillis()}_${imageUri.lastPathSegment}"
        val imageRef = storageRef.child(imageLabel)
        val uploadTask = imageRef.putFile(imageUri)


        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = dateFormat.format(currentDate)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl

        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val downloadUrl = task.result.toString()
                val prodId = dbRef.push().key

                val prods = Products(
                    prodImage = downloadUrl,
                    userid = currentUser,
                    dateAdded = formattedDate
                )
                if (downloadUrl != null && currentUser != null) {
                    if (prodId != null) {
                        dbRef.child(prodId).setValue(prods)
                            .addOnCompleteListener {
                                Toast.makeText(
                                    this,
                                    "Product Added Successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                var intent = Intent(this, ProductsActivity::class.java)
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
}