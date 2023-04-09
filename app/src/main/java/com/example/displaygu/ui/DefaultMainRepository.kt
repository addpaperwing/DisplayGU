package com.example.displaygu.ui

import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import com.example.displaygu.network.Api
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: Api,
    private val dispatcher: CoroutineDispatcher
): MainRepository {

    override suspend fun getData(name: String): Flow<Pair<User, List<Repo>>> = flow {
        emit(Pair(api.getUser(name), api.getRepos(name)))
    }.flowOn(dispatcher)
}