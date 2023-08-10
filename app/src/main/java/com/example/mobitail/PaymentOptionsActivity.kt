package com.example.mobitail

import android.content.Intent
import com.example.mobitail.BuildConfig
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.example.mobitail.consumer.mainActivities.HomeActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class PaymentOptionsActivity : AppCompatActivity() {

    private val paystackSecretKey = BuildConfig.PSTK_PRIVATE_KEY
    private lateinit var mCardNumber: TextInputEditText
    private lateinit var mCardExpiry: TextInputEditText
    private lateinit var mCardCVV: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_options)

        initializePaystack()
        initializeFormVariables()
    }

    private fun initializeFormVariables() {
        mCardNumber = findViewById(R.id.til_card_number)
        mCardExpiry = findViewById(R.id.til_card_expiry)
        mCardCVV = findViewById(R.id.til_card_cvv)

        mCardExpiry!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?,
                                           start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length == 2 && !s.toString().contains("/")) {
                    s!!.append("/")
                }
            }
        })

        val button = findViewById<Button>(R.id.btnStartPayment)
        button.setOnClickListener { v: View? -> performCharge() }
    }

    private fun performCharge() {
        val cardNumber = mCardNumber!!.text.toString()
        val cardExpiry = mCardExpiry!!.text.toString()
        val cvv = mCardCVV!!.text.toString()

        val cardExpiryArray = cardExpiry.split("/").toTypedArray()
        val expiryMonth = cardExpiryArray[0].toInt()
        val expiryYear = cardExpiryArray[1].toInt()
        var amount = 30
        amount *= 100

        val card = Card(cardNumber, expiryMonth, expiryYear, cvv)

        val charge = Charge()
        charge.amount = intent.getIntExtra("amount", 1)
        charge.email = FirebaseAuth.getInstance().currentUser!!.email
        charge.card = card
        charge.currency = "KES"

        PaystackSdk.chargeCard(this, charge, object : Paystack.TransactionCallback {
            override fun onSuccess(transaction: Transaction) {
                val transactionReference = transaction.reference
                sendTransactionReferenceToServer(transactionReference)
                parseResponse(transactionReference)

                val homeIntent = Intent(this@PaymentOptionsActivity, HomeActivity::class.java)
                startActivity(homeIntent)
                clearcart()
                finish()
            }

            override fun beforeValidate(transaction: Transaction) {
                Log.d("Main Activity", "beforeValidate: " + transaction.reference)
            }

            override fun onError(error: Throwable, transaction: Transaction) {
                Log.d("Main Activity", "onError: " + error.localizedMessage)
                Log.d("Main Activity", "onError: $error")
            }
        })
    }

    private fun initializePaystack() {
        PaystackSdk.initialize(applicationContext)
        PaystackSdk.setPublicKey(BuildConfig.PSTK_PUBLIC_KEY)
    }

    private fun parseResponse(transactionReference: String) {
        val message = "Payment Successful - $transactionReference"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun sendTransactionReferenceToServer(transactionReference: String) {
        val url = "http://172.26.96.1:3000/transactions"

        val client = OkHttpClient()

        val json = """{"transaction_reference": "$transactionReference"}"""
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                // Handle response if needed
                if (response.isSuccessful) {
                    Log.d("PaymentOptionsActivity", "Transaction reference sent successfully")
                } else {
                    Log.e("PaymentOptionsActivity", "Failed to send transaction reference")
                }

            }
        })
    }

    private fun clearcart(){}

}

