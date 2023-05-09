package com.apw.ql.data

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

//{
//    "name":"MarkdownView",
//    "owner":{
//        "login":"tiagohm",
//        "avatar_url":"https://avatars.githubusercontent.com/u/16683610?v=4"
//    },
//    "html_url":"https://github.com/tiagohm/MarkdownView",
//    "description":"Android Markdown编辑器",
//    "topics":[
//        "android",
//        "android-markdown",
//        "mvp",
//        "rxjava"
//    ],
//    "language":"Java",
//    "stargazers_count":1453,
//    "updated_at":"2023-05-02T12:30:51Z"
//}
@JsonClass(generateAdapter = true)
@Parcelize
data class Repo(
    val name: String,
    val owner: User,
    @field:Json(name = "html_url") val htmlUrl: String,
    val description: String? = null,
    val topics: List<String>,
    val language: String?,
    @field:Json(name = "stargazers_count") val stargazersCount: Int,
    @field:Json(name = "updated_at") val updateAt: String,
): Parcelable