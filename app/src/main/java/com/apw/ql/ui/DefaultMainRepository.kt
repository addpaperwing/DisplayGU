package com.apw.ql.ui

import com.apw.ql.data.model.Repo
import com.apw.ql.data.remote.Api
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: Api,
    private val dispatcher: CoroutineDispatcher
): MainRepository {

    override suspend fun getData(query: String, page: Int, sort: String?): Flow<List<Repo>> = flow {
        emit(api.searchRepositories(query, page, sort).items)
    }.flowOn(dispatcher)
}