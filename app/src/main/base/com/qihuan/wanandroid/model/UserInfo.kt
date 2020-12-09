/* (C)2020 */
package com.qihuan.wanandroid.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class UserInfo(
    @Json(name = "admin")
    val admin: Boolean, // false
    @Json(name = "chapterTops")
    val chapterTops: List<Any>,
    @Json(name = "coinCount")
    val coinCount: Int, // 0
    @Json(name = "collectIds")
    val collectIds: List<Int>,
    @Json(name = "email")
    val email: String,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "id")
    val id: Int, // 83063
    @Json(name = "nickname")
    val nickname: String, // qihuan1
    @Json(name = "password")
    val password: String,
    @Json(name = "publicName")
    val publicName: String, // qihuan1
    @Json(name = "token")
    val token: String,
    @Json(name = "type")
    val type: Int, // 0
    @Json(name = "username")
    val username: String // qihuan1
)
