/* (C)2020 */
package com.qihuan.wanandroid.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class PageData<T>(
    @Json(name = "curPage")
    val curPage: Int, // 1
    @Json(name = "datas")
    val dataList: List<T>,
    @Json(name = "offset")
    val offset: Int, // 0
    @Json(name = "over")
    val over: Boolean, // false
    @Json(name = "pageCount")
    val pageCount: Int, // 483
    @Json(name = "size")
    val size: Int, // 20
    @Json(name = "total")
    val total: Int, // 9654
)
