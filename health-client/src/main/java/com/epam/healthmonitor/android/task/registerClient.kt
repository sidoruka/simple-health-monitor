package com.epam.healthmonitor.android.task

import android.util.Log
import com.epam.healthmonitor.android.service.Client
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.math.roundToInt

private const val TAG = "client registering"
private fun randomSuffix() = (Math.random() * 100_000).roundToInt()

fun registerClient(endpoint: String,
                   firebaseToken: String,
                   firebaseTopic: String
): DistributedTask<Boolean> = doAsync {
    var registered = false
    try {
        val deviceName = "device-name-${randomSuffix()}"

        val response = Client.use(endpoint)
            .registerDevice(deviceName, firebaseToken)

        if (response.isSuccessful) {
            Log.d(TAG, "Device was registered")

            FirebaseMessaging.getInstance().subscribeToTopic(firebaseTopic)
            Log.d(TAG, "Device was subscribed to $firebaseTopic topic")

            registered = true
        } else {
            Log.d(TAG, "Device wasn't registered due to: ${response.errorBody()?.string()}")
        }
    } catch (e: Throwable) {
        Log.d(TAG, "Device wasn't registered due to: %s".format(e))
    }
    registered
}