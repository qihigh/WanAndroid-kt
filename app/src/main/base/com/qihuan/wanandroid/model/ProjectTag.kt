/* (C)2020 */
package com.qihuan.wanandroid.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ProjectTag(
    @Json(name = "name")
    val name: String, // 项目
    @Json(name = "url")
    val url: String // /project/list/1?cid=539
)
