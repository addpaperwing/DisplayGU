package com.example.displaygu.network

import retrofit2.HttpException

enum class Status {
    LOADING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class TaskState private constructor(
    val status: Status,
    val exception: Throwable? = null
) {

    fun getErrorMessage(): String? {
        return if (exception is HttpException) {
            exception.response()?.errorBody()?.string()
        } else {
            exception?.message
        }
    }
    companion object {
        val SUCCEED = TaskState(Status.SUCCESS)
        val LOADING = TaskState(Status.LOADING)
        fun error(e: Throwable) = TaskState(Status.FAILED, e)
    }
}


