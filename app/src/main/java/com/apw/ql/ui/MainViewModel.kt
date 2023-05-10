package com.apw.ql.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apw.ql.data.model.Repo
import com.apw.ql.data.remote.State
import com.apw.ql.exts.retryAndCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _result = MutableLiveData<State<List<Repo>>>()

    val result get() = _result

    private var page = 0
    fun getData(query: String, sort: String?, refresh: Boolean) {
        if (refresh) page = 0
        viewModelScope.launch {
            repository.getData(query, page++ ,sort).retryAndCatch(_result).collect {
                _result.value = State.Success(it)
            }
        }
    }
}