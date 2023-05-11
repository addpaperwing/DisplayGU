package com.apw.ql.ui.main

import com.apw.ql.data.model.Repo
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getData(query: String, sort: String?): Flow<List<Repo>>
}