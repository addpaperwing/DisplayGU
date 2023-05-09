package com.apw.ql.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class User(
    @field:Json(name = "login") val name: String? = null,
    @field:Json(name = "avatar_url") val avatarUrl: String? = null
): Parcelable