package com.example.displaygu.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import com.example.displaygu.util.TaskState
import com.example.displaygu.util.collectWithTaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val taskState = MutableLiveData<TaskState>()
    val result = MutableLiveData<Pair<User, List<Repo>>>()

    fun getData(userName: String) {
        viewModelScope.launch {
            repository.getUser(userName).zip(repository.getRepos(userName)) {user, repos ->
                Pair(user, repos)
            }.collectWithTaskState(taskState) {
                result.value = it
            }
        }
    }

}