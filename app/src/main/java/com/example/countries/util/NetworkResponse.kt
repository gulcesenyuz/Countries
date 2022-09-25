package com.example.countries.util

sealed class NetworkResponse<T>(
    val data: T? = null,
    val exception: String? =null
) {
    class Success<T>(data: T?) : NetworkResponse<T>(data)
    class Error<T>(exception: String?) : NetworkResponse<T>(null, exception)
    class Loading<T> : NetworkResponse<T>(null,null)
}