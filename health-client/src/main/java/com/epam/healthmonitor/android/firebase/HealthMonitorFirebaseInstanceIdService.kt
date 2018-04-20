package com.epam.healthmonitor.android.firebase

import android.preference.PreferenceManager
import android.util.Log
import com.epam.healthmonitor.android.R
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class HealthMonitorFirebaseInstanceIdService
    : FirebaseInstanceIdService() {

    companion object {
        const val TAG = "fb instance service"
    }

    override fun onTokenRefresh() {
        FirebaseInstanceId.getInstance()
            .token
            ?.also { deviceId ->
                Log.d(TAG, "Received firebase token: $deviceId")

                PreferenceManager
                    .getDefaultSharedPreferences(applicationContext)
                    .edit()
                    .putString(applicationContext.getString(R.string.firebase_token_key), deviceId)
                    .apply()
            }
            ?: Log.d(TAG, "Firebase token wasn't received")
    }
}