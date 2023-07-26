package com.example.mobitail.retailer.mainActivities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Products

import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import java.util.Date
import java.text.SimpleDateFormat


class AddRemoveProduct : AppCompatActivity() {
    private lateinit var imagePreview: ImageView
    private lateinit var addProd: MaterialButton
    private lateinit var selectProdImg: MaterialButton
    private lateinit var prodLabel: EditText
    private lateinit var imageUri: Uri
    private lateinit var storageRef: StorageReference
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_remove_product)

        selectProdImg = findViewById(R.id.selectImageBtn)
        addProd = findViewById(R.id.uploadProduct)
        imagePreview = findViewById(R.id.imagePreview)
        prodLabel = findViewById(R.id.prod_name)
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
        val name = prodLabel.text.toString()


        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = dateFormat.format(currentDate)

        if (name != null){
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val downloadUrl = task.result.toString()
                    val prodId = dbRef.push().key

                    if (downloadUrl != null && currentUser != null) {
                        if (prodId != null) {
                            getStore(currentUser) { key ->
                                val prods = Products(
                                    prodName = name,
                                    prodImage = downloadUrl,
                                    storeId = key,
                                    userid = currentUser,
                                    dateAdded = formattedDate
                                )
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
                    }

                } else {
                    Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
                }
            }
            prodLabel.text.clear()
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

    private fun getStore(userid: String, callback: (String?) -> Unit) {
        val storedb = FirebaseDatabase.getInstance().getReference("stores")
        val query: Query = storedb.orderByChild("userid").equalTo(userid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val key = dataSnapshot.children.first().key
                    callback(key)
                } else {
                    // Handle the case where no matching record was found
                    println("No record found with id: $userid")
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
                callback(null)
            }
        })
    }
}