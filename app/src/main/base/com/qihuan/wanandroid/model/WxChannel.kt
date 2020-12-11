/* (C)2020 */
package com.qihuan.wanandroid.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class WxChannel(
    @Json(name = "courseId")
    val courseId: Int, // 13
    @Json(name = "id")
    val id: Int, // 408
    @Json(name = "name")
    val name: String, // 鸿洋
    @Json(name = "order")
    val order: Int, // 190000
    @Json(name = "parentChapterId")
    val parentChapterId: Int, // 407
    @Json(name = "userControlSetTop")
    val userControlSetTop: Boolean, // false
    @Json(name = "visible")
    val visible: Int // 1
)
