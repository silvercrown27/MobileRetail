package com.example.mobitail

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log


class Notifications : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "Refreshed token: $token")
        saveTokenToDatabase(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { notification ->

            val title = notification.title
            val message = notification.body

            showNotification(title, message)
        }
    }

    private fun saveTokenToDatabase(token: String) {
        // TODO: Save the token to your Firebase Realtime Database
    }

    private fun showNotification(title: String?, message: String?) {
        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.chat)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel Name",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}
