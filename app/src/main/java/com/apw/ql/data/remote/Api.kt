package com.apw.ql.data.remote

import com.apw.ql.data.model.ListResponse
import com.apw.ql.data.model.Repo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("/search/repositories")
    suspend fun searchRepositories(@Query("q") query: String,
                                   @Query("page") page: Int,
                                   @Query("sort") sort: String? = null,
                                   @Query("per_page") perPage: Int = 10): ListResponse<Repo>

    @GET("/repos/{owner}/{repo}/readme")
    suspend fun getReadme(@Path("owner") owner: String, @Path("repo") repo: String)

}