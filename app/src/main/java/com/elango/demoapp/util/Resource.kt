package com.elango.demoapp.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Loading<T> : Resource<ArrayList<T>>()
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

}
