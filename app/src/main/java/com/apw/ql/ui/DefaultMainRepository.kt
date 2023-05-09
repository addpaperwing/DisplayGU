package com.apw.ql.ui

import com.apw.ql.data.ListResponse
import com.apw.ql.data.Repo
import com.apw.ql.data.User
import com.apw.ql.network.Api
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: Api,
    private val dispatcher: CoroutineDispatcher
): MainRepository {

    override suspend fun getData(query: String, sort: String?): Flow<List<Repo>> = flow {
        emit(api.searchRepositories(query, sort).items)
    }.flowOn(dispatcher)
}