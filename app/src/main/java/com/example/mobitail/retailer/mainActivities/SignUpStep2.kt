package com.example.mobitail.retailer.mainActivities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Stores
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hbb20.CountryCodePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SignUpStep2 : AppCompatActivity() {
    private lateinit var storeName: TextInputEditText
    private lateinit var storeEmail: TextInputEditText
    private lateinit var storeNumber: TextInputEditText
    private lateinit var storeLocation: TextInputEditText
    private lateinit var numberPicker: CountryCodePicker
    private lateinit var storedb: DatabaseReference
    private lateinit var next_btn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_step2)

        storedb = FirebaseDatabase.getInstance().getReference("stores")

        storeName = findViewById(R.id.storeName)
        storeEmail = findViewById(R.id.storeEmail)
        storeNumber = findViewById(R.id.storeNumber)
        storeLocation = findViewById(R.id.storeLocation)
        numberPicker = findViewById(R.id.country_code_picker)
        next_btn = findViewById(R.id.NextStep)

        next_btn.setOnClickListener {
            val fields = listOf(storeName, storeEmail, storeNumber, storeLocation)
            val fieldNames = listOf("name", "email", "number", "location")
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
                val store_name = fields[0].text.toString().trim()
                val store_email = fields[1].text.toString().trim()
                val store_num = fields[2].text.toString().trim()
                val store_loc = fields[3].text.toString().trim()

                val currentDate = Date()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val dateString = dateFormat.format(currentDate)
                val timeString = timeFormat.format(currentDate)
                val dateTimeString = "$dateString - $timeString"

                val store = Stores(
                    storename = store_name,
                    storeemail = store_email,
                    storenumber = store_num,
                    storelocation = store_loc,
                    userid = intent.getStringExtra("user")!!,
                    doc = dateTimeString
                )
                addRecords(store)

            }
        }

//        back.setOnClickListener {
//            var intent = Intent(this, StartUpActivity::class.java)
//            startActivity(intent)
//
//            overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
//    }
    }

    private fun addRecords(store: Stores) {
        val storeid = storedb.push().key!!

        storedb.child(storeid).setValue(store)
            .addOnCompleteListener {
                Toast.makeText(
                    this,
                    "Store Registered Successfully!",
                    Toast.LENGTH_SHORT
                ).show()

                var intent = Intent(this, FinalSignupStep::class.java)
                intent.putExtra("storeid", storeid)
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