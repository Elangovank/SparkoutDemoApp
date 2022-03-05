package com.elango.demoapp.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elango.demoapp.model.User
import com.elango.demoapp.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.suguna.broilerdoc.api.Responses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {


    private val data = MutableLiveData<ArrayList<Int>?>()
    val postdata: LiveData<ArrayList<Int>?> = data

    private val userDetails = MutableLiveData<User?>()
    val _UserDetailsLD: LiveData<User?> = userDetails

    init {
        getData("v0/askstories.json?print=pretty")
    }

    fun getData(
        url: String
    ) {
        viewModelScope.launch {
            try {
                userRepository.getDataNew(url)
                    .enqueue(object : Callback<ResponseBody?> {
                        override fun onResponse(
                            call: Call<ResponseBody?>,
                            response: Response<ResponseBody?>
                        ) {
                            val body = response.body()?.string()

                            val userListType: Type = object :
                                TypeToken<ArrayList<Int?>?>() {}.type
                            val objec: ArrayList<Int> =
                                Gson().fromJson(body, userListType)
                            val result = Responses()
                            result.responseStatus = true
                            result.responseMessage = "Success"
                            data.postValue(objec)
                        }

                        override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {

                            val result = Responses()
                            result.responseStatus = false
                            result.responseMessage = "Failure"

                            data.postValue(null)
                        }
                    })

            } catch (e: Exception) {
                e.printStackTrace()
                val result = Responses()
                result.responseStatus = false
                result.responseMessage = "Failure"
                data.postValue(null)
            }
        }
    }

    fun getUserDetails(
        userID: String
    ) {
        viewModelScope.launch {
            try {
                userRepository.getDataNew("v0/item/$userID.json?print=pretty")
                    .enqueue(object : Callback<ResponseBody?> {
                        override fun onResponse(
                            call: Call<ResponseBody?>,
                            response: Response<ResponseBody?>
                        ) {
                            val body = response.body()?.string()
                            body?.let { Log.e("Data", it) }
                            val result = Gson().fromJson(body, User::class.java)
                            result.responseStatus = true
                            result.responseMessage = "Success"
                            userDetails.postValue(result)
                        }

                        override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {

                            val result = Responses()
                            result.responseStatus = false
                            result.responseMessage = "Failure"
                            userDetails.postValue(null)
                        }
                    })

            } catch (e: Exception) {
                e.printStackTrace()
                val result = Responses()
                result.responseStatus = false
                result.responseMessage = "Failure"
                userDetails.postValue(null)
            }
        }


    }
}