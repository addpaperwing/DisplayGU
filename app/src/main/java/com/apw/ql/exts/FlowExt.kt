package com.apw.ql.exts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apw.ql.data.remote.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

inline fun <T> Flow<T>.retryAndCatch(
    liveData: MutableLiveData<State<T>>,
    delay: Long = 2000,
    crossinline retryCondition: (cause: Throwable, attempt: Long) -> Boolean = {cause, attempt ->
        cause is IOException && attempt < 3
    },
    logTag: String = "Flow"
): Flow<T> {
    return onStart {
        liveData.value = State.Loading
    }.retryWhen { cause, attempt ->
        delay(delay)
        return@retryWhen retryCondition(cause, attempt)
    }.catch { cause ->
        Log.e(logTag, "Error: ", cause)
        liveData.value = State.Error(cause)
    }
}