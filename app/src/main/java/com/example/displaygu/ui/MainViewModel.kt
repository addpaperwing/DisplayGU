package com.example.displaygu.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import com.example.displaygu.network.TaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _taskState = MutableLiveData<TaskState>()
    private val _result = MutableLiveData<Pair<User, List<Repo>>>()

    val result get() = _result
    val taskState get() = _taskState

    fun getData(userName: String) {
        viewModelScope.launch {
            repository.getData(userName)
                .onStart {
                    _taskState.postValue(TaskState.LOADING)
                }.catch { cause ->
                    Log.e(TAG, "Error", cause)
                    _taskState.postValue(TaskState.error(cause))
                }.collect {
                    _result.value = it
                    _taskState.postValue(TaskState.SUCCEED)
                }
        }
    }
}