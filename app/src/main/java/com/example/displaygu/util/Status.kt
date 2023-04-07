package com.example.displaygu.util

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

    companion object {
        val SUCCEED = TaskState(Status.SUCCESS)
        val LOADING = TaskState(Status.LOADING)
        fun error(e: Throwable) = TaskState(Status.FAILED, e)
    }
}


