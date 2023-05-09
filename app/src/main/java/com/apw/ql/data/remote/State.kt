package com.apw.ql.data.remote

import retrofit2.HttpException

sealed class State<out T> {
    object Loading : State<Nothing>()

    data class Error(val exception: Throwable) : State<Nothing>() {

        fun getErrorMessage(): String? {
            return if (exception is HttpException) {
                exception.response()?.errorBody()?.string()
            } else {
                exception.message
            }
        }
    }

    data class Success<out T>(val data: T) : State<T>()
}