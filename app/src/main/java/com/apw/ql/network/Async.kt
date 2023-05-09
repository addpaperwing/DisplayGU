package com.apw.ql.network

import retrofit2.HttpException

sealed class Async<out T> {
    object Loading : Async<Nothing>()

    data class Error(val exception: Throwable) : Async<Nothing>() {

        fun getErrorMessage(): String? {
            return if (exception is HttpException) {
                exception.response()?.errorBody()?.string()
            } else {
                exception.message
            }
        }
    }

    data class Success<out T>(val data: T) : Async<T>()
}