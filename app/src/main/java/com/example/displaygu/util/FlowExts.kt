package com.example.displaygu.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.*
import com.example.displaygu.util.TaskState

private const val TAG = "flow"

suspend fun <T> Flow<T>.collectWithTaskState(taskState: MutableLiveData<TaskState>, collect: (result: T) -> Unit) {
    return onStart {
        taskState.postValue(TaskState.LOADING)
    }.catch { cause ->
        Log.e(TAG, "Error", cause)
        taskState.postValue(TaskState.error(cause))
    }.collect {
        collect.invoke(it)
        taskState.postValue(TaskState.SUCCEED)
    }
}
