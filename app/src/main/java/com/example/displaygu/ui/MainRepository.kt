package com.example.displaygu.ui

import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import kotlinx.coroutines.flow.Flow


interface MainRepository {
    suspend fun getData(name: String): Flow<Pair<User, List<Repo>>>
}