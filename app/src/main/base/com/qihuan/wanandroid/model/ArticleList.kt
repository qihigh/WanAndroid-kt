package com.qihuan.wanandroid.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep

@Keep
@JsonClass(generateAdapter = true)
data class ArticleList(
    @Json(name = "curPage")
    val curPage: Int, // 1
    @Json(name = "datas")
    val articles: List<Article>,
    @Json(name = "offset")
    val offset: Int, // 0
    @Json(name = "over")
    val over: Boolean, // false
    @Json(name = "pageCount")
    val pageCount: Int, // 483
    @Json(name = "size")
    val size: Int, // 20
    @Json(name = "total")
    val total: Int // 9654
)