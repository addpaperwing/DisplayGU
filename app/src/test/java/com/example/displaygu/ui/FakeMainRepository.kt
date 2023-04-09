package com.example.displaygu.ui

import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class FakeMainRepository(
    private val user: User,
    private val exception: HttpException,
    private vararg val repos: Repo,
): MainRepository {
    override suspend fun getData(name: String): Flow<Pair<User, List<Repo>>> = flow {
        delay(300)
        if (name == user.name) {
            emit(Pair(user, repos.asList()))
        } else {
            throw exception
        }
    }
}