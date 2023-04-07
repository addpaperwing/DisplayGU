package com.example.displaygu.network

import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("/users/{userName}")
    suspend fun getUser(@Path("userName") userName: String): User

    @GET("/users/{userName}/repos")
    suspend fun getRepos(@Path("userName") userName: String): List<Repo>


}