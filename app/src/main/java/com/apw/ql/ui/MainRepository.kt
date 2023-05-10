package com.apw.ql.ui

import com.apw.ql.data.model.Repo
import kotlinx.coroutines.flow.Flow


interface MainRepository {
    suspend fun getData(query: String, page: Int, sort: String?): Flow<List<Repo>>
}