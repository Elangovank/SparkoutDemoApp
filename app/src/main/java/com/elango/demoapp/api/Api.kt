package com.elango.demoapp.api

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url


interface Api {

    @GET()
    fun getCall(@Url url: String): Call<ResponseBody?>
}