package com.example.displaygu.api

import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import com.example.displaygu.network.Api
import retrofit2.HttpException
class FakeApi(
    private val user: User,
    private val exception: HttpException,
    private vararg val repos: Repo
) : Api {
    override suspend fun getUser(userName: String): User {
        return if (userName == user.name) user else throw exception
    }
    override suspend fun getRepos(userName: String): List<Repo> {
        return if (userName == user.name) {
            ArrayList<Repo>().apply {
                addAll(repos)
            }
        } else {
            emptyList()
        }
    }
}