package com.qihuan.wanandroid.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep

@Keep
@JsonClass(generateAdapter = true)
data class ArticleTag(
    @Json(name = "name")
    val name: String, // 项目
    @Json(name = "url")
    val url: String, // /project/list/1?cid=294
)