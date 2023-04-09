package com.example.displaygu.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

//{
// "name" : String,
// "description" : String,
// "updated_at" : String,
// "stargazers_count": Integer,
// "forks" : Integer
// }
@JsonClass(generateAdapter = true)
@Parcelize
data class Repo(
    val name: String,
    val description: String? = null,
    @field:Json(name = "updated_at") val updateAt: String,
    @field:Json(name = "stargazers_count") val stargazersCount: Int,
    val forks: Int
): Parcelable {
}