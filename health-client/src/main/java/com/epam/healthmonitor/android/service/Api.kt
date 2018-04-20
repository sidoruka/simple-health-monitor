package com.epam.healthmonitor.android.service

import com.epam.healthmonitor.android.model.Metric
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PUT

interface Api {

    @GET("/metrics")
    fun metrics(): Call<List<Metric>>

    @FormUrlEncoded
    @PUT("/devices")
    fun devices(@Field("device_name")
                deviceName: String,
                @Field("device_id")
                deviceId: String
    ): Call<ResponseBody>
}