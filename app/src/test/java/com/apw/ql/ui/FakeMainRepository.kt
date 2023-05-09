package com.apw.ql.ui

import com.apw.ql.data.model.Repo
import com.apw.ql.data.model.User
import com.apw.ql.ui.main.MainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class FakeMainRepository(
    private val ioException: IOException,
    private val exception: HttpException,
    private vararg val repos: Repo
): MainRepository {

    override suspend fun getData(query: String, sort: String?): Flow<List<Repo>> = flow {
        delay(300)
        when(query) {
            "repo1" -> {
                emit(repos.asList())
            }
            "ioException" -> {
                throw ioException
            }
            else -> {
                throw exception
            }
        }
    }
}