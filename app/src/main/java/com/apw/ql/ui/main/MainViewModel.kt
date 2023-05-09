package com.apw.ql.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apw.ql.data.model.Repo
import com.apw.ql.data.remote.State
import com.apw.ql.exts.retryAndCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _result = MutableLiveData<State<List<Repo>>>()

    val result get() = _result
    fun getData(query: String, sort: String?) {
        viewModelScope.launch {
            repository.getData(query ,sort).retryAndCatch(_result).collect {
                _result.value = State.Success(it)
            }
        }
    }
}