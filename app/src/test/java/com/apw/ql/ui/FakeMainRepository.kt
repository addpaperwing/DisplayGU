package com.apw.ql.ui

import com.apw.ql.data.model.Repo
import com.apw.ql.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class FakeMainRepository(
    private val user: User,
    private val exception: HttpException,
    private vararg val repos: Repo,
): MainRepository {
    override suspend fun getData(name: String): Flow<List<Repo>> = flow {
        delay(300)
        if (name == user.name) {
            emit(repos.asList())
        } else {
            throw exception
        }
    }
}