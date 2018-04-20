package com.epam.healthmonitor.android.service

import android.content.Context
import android.preference.PreferenceManager
import com.epam.healthmonitor.android.R
import com.epam.healthmonitor.android.model.Metric
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object Client {

    private const val TAG: String = "health monitor client"

    /**
     * Initializes after what endpoint to use is specified.
     * @see use
     */
    private var currentEndpoint = ""

    /**
     * Initializes after what endpoint to use is specified.
     * @see use
     */
    private lateinit var api : Api

    fun fetchMetrics(): Collection<Metric> =
            api.metrics()
                .execute()
                .body()
                .orEmpty()

    fun registerDevice(deviceName: String,
                       deviceId: String): Response<ResponseBody> =
            api.devices(deviceName, deviceId)
                .execute()

    fun use(endpoint: String) : Client {
        if (endpoint.isNotBlank() && endpoint != currentEndpoint) {
            currentEndpoint = endpoint
            api = Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build()
                    .create(Api::class.java)
        }
        return this
    }

    fun fromPreferences(context: Context): Client =
        use(endpoint(context))

    private fun endpoint(context: Context): String {
        val key = context.getString(R.string.endpoint_key)
        val defaultValue = context.getString(R.string.endpoint_default_value)
        return PreferenceManager
            .getDefaultSharedPreferences(context)
            .getString(key, defaultValue)
    }
}
