package com.example.displaygu.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//{
// "name" : String,
// "description" : String,
// "updated_at" : String,
// "stargazers_count": Integer,
// "forks" : Integer
// }
@JsonClass(generateAdapter = true)
data class Repo(
    val name: String,
    val description: String?,
    val forks: Int,
    @field:Json(name = "updated_at") val updateAt: String,
    @field:Json(name = "stargazers_count") val stargazersCount: String,
) {
}