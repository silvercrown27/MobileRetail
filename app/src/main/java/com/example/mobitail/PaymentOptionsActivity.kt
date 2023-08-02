package com.example.mobitail

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.pesapal.pesapalandroid.data.Payment
import com.google.gson.Gson
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PaymentOptionsActivity : AppCompatActivity() {

    private lateinit var amountText: EditText
    private lateinit var btnStartPayment: MaterialButton
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_options)

        amountText = findViewById(R.id.editAmount)
        btnStartPayment = findViewById(R.id.btnStartPayment)
        webView = findViewById(R.id.webView)

        btnStartPayment.setOnClickListener {
            val amount = amountText.text.toString().toDoubleOrNull()
            if (amount != null && amount > 0.0) {
                startPaymentProcess(amount)
            } else {
                Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startPaymentProcess(amount: Double) {
        val consumerKey = getString(R.string.pesapal_consumer_key)
        val consumerSecret = getString(R.string.pesapal_consumer_secret)
        val callbackUrl = "https://www.myapplication.com/response-page"
        val notificationId = "fe078e53-78da-4a83-aa89-e7ded5c456e6"

        val orderId = "ORDER_${System.currentTimeMillis()}"
        val currency = "KES"

        PesapalApiClient.submitOrderRequest(
            orderId,
            currency,
            amount,
            "Payment for Order $orderId",
            callbackUrl,
            notificationId,
            object : PesapalApiClient.PaymentListener {
                override fun onPaymentSuccess(paymentUrl: String) {
                    // Show the WebView and load the payment URL
                    webView.visibility = View.VISIBLE
                    webView.loadUrl(paymentUrl)

                    // Optionally, you can handle payment success within the WebView
                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            if (url?.contains("/pesapal-mobile-checkout/redirect-mobile") == true) {
                                // Payment success
                                // You can handle the success logic here, e.g., show a success message
                                Toast.makeText(this@PaymentOptionsActivity, "Payment successful!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                override fun onPaymentError(errorMessage: String) {
                    // Handle payment error
                    Toast.makeText(this@PaymentOptionsActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
