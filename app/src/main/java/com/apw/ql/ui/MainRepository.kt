package com.apw.ql.ui

import com.apw.ql.data.ListResponse
import com.apw.ql.data.Repo
import com.apw.ql.data.User
import kotlinx.coroutines.flow.Flow


interface MainRepository {
    suspend fun getData(query: String, sort: String?): Flow<List<Repo>>
}