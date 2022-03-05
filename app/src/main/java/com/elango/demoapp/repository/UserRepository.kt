package com.elango.demoapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.elango.demoapp.api.Api
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject


class UserRepository @Inject constructor(private val api: Api) {

    fun getDataNew(url: String) = api.getCall(url)


}
