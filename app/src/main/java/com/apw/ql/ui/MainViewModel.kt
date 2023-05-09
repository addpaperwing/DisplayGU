package com.apw.ql.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apw.ql.data.Repo
import com.apw.ql.data.User
import com.apw.ql.network.Async
import com.apw.ql.network.retryAndCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _result = MutableLiveData<Async<List<Repo>>>()

    val result get() = _result

    fun getData(query: String, sort: String?) {
        viewModelScope.launch {
            repository.getData(query, sort).retryAndCatch(_result).collect {
                _result.value = Async.Success(it)
            }
        }
    }
}