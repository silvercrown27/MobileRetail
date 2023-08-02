package com.example.mobitail

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

object PesapalApiClient {

    private val BASE_URL = "https://cybqa.pesapal.com/pesapalv3/api/"

    private val client = OkHttpClient()
    private val gson = Gson()
    private val mainHandler = Handler(Looper.getMainLooper())

    private val accessToken = "<your_access_token>"

    data class PaymentResponse(
        val order_tracking_id: String,
        val merchant_reference: String,
        val redirect_url: String,
        val error: Any?,
        val status: String
    )

    fun submitOrderRequest(
        id: String,
        currency: String,
        amount: Double,
        description: String,
        callbackUrl: String,
        notificationId: String,
        listener: PaymentListener
    ) {
        val url = "$BASE_URL/Transactions/SubmitOrderRequest"

        val billingAddress = mapOf(
            "email_address" to "bradabwao14@gmail.com",
            "first_name" to "John", // Change this to the customer's first name
            "last_name" to "Doe" // Change this to the customer's last name
            // Add other billing address details if needed
        )

        val requestParams = mapOf(
            "id" to id,
            "currency" to currency,
            "amount" to amount,
            "description" to description,
            "callback_url" to callbackUrl,
            "notification_id" to notificationId,
            "billing_address" to billingAddress
        )

        val requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(requestParams))
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $accessToken")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()?.string()
                val paymentResponse = gson.fromJson(responseBody, PaymentResponse::class.java)

                mainHandler.post {
                    if (response.isSuccessful && paymentResponse != null) {
                        listener.onPaymentSuccess(paymentResponse.redirect_url)
                    } else {
                        listener.onPaymentError("Payment request failed.")
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    listener.onPaymentError("Failed to connect to the server.")
                }
            }
        })
    }

    interface PaymentListener {
        fun onPaymentSuccess(paymentUrl: String)
        fun onPaymentError(errorMessage: String)
    }
}
