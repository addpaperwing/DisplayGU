package com.apw.ql.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//{
//total_count: 296,
//incomplete_results: false,
//items: []
//}

@JsonClass(generateAdapter = true)
data class ListResponse<T>(
//    @field:Json(name = "total_count") val totalCount: Int,
//    @field:Json(name = "incomplete_results") val incompleteResults: Boolean,
    val items: List<T>
) {
}