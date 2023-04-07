package com.example.displaygu.ui

import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import kotlinx.coroutines.flow.Flow


interface MainRepository {

    suspend fun getUser(name: String): Flow<User>
    suspend fun getRepos(name: String): Flow<List<Repo>>
}