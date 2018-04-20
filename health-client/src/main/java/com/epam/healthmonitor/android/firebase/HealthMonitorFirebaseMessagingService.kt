package com.epam.healthmonitor.android.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat.getColor
import android.util.Log
import com.epam.healthmonitor.android.MainActivity
import com.epam.healthmonitor.android.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class HealthMonitorFirebaseMessagingService
    : FirebaseMessagingService() {

    companion object {
        const val TAG = "fb messaging service"
        const val notificationsChannelId = "health_monitor_channel"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val firebaseNotification = message.notification
        val notificationText = firebaseNotification?.body ?: "empty body"
        Log.d(TAG, "Received notification with text: $notificationText")

        sendNotification(firebaseNotification)
    }

    private fun sendNotification(firebaseNotification: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
                this,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT
        )

        val notification = NotificationCompat.Builder(this, notificationsChannelId)
            .setSmallIcon(R.mipmap.ic_foreground)
            .setColor(getColor(this, R.color.iconBackground))
            .setContentTitle(getString(R.string.app_name))
            .setContentText(firebaseNotification.body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        // Since android 8.0 notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    notificationsChannelId,
                    "Firebase messaging service channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = Random().nextInt(1000)
        notificationManager.notify(notificationId, notification)
    }
}