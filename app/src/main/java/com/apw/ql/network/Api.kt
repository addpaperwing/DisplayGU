package com.apw.ql.network

import com.apw.ql.data.ListResponse
import com.apw.ql.data.Repo
import com.apw.ql.data.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

//    @GET("/users/{userName}")
//    suspend fun getUser(@Path("userName") userName: String): User
//
//    @GET("/users/{userName}/repos")
//    suspend fun getRepos(@Path("userName") userName: String): List<Repo>

    @GET("/search/repositories")
    suspend fun searchRepositories(@Query("q") query: String, @Query("sort") sort: String? = null, @Query("per_page") perPage: Int = 10): ListResponse<Repo>

    @GET("/repos/{owner}/{repo}/readme")
    suspend fun getReadme(@Path("owner") owner: String, @Path("repo") repo: String)

}