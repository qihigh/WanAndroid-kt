/* (C)2020 */
package com.qihuan.wanandroid.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Article(
    @Json(name = "apkLink")
    val apkLink: String,
    @Json(name = "audit")
    val audit: Int, // 1
    @Json(name = "author")
    val author: String,
    @Json(name = "canEdit")
    val canEdit: Boolean, // false
    @Json(name = "chapterId")
    val chapterId: Int, // 502
    @Json(name = "chapterName")
    val chapterName: String, // 自助
    @Json(name = "collect")
    val collect: Boolean, // false
    @Json(name = "courseId")
    val courseId: Int, // 13
    @Json(name = "desc")
    val desc: String,
    @Json(name = "descMd")
    val descMd: String,
    @Json(name = "envelopePic")
    val envelopePic: String,
    @Json(name = "fresh")
    val fresh: Boolean, // true
    @Json(name = "id")
    val id: Int, // 16382
    @Json(name = "link")
    val link: String, // https://juejin.cn/post/6902793228026642446
    @Json(name = "niceDate")
    val niceDate: String, // 16分钟前
    @Json(name = "niceShareDate")
    val niceShareDate: String, // 16分钟前
    @Json(name = "origin")
    val origin: String,
    @Json(name = "prefix")
    val prefix: String,
    @Json(name = "projectLink")
    val projectLink: String,
    @Json(name = "publishTime")
    val publishTime: Long, // 1607496167000
    @Json(name = "realSuperChapterId")
    val realSuperChapterId: Int, // 493
    @Json(name = "selfVisible")
    val selfVisible: Int, // 0
    @Json(name = "shareDate")
    val shareDate: Long?, // 1607496167000
    @Json(name = "shareUser")
    val shareUser: String, // qwerhuan
    @Json(name = "superChapterId")
    val superChapterId: Int, // 494
    @Json(name = "superChapterName")
    val superChapterName: String, // 广场Tab
    @Json(name = "tags")
    val tags: List<ArticleTag>,
    @Json(name = "title")
    val title: String, // 深入剖析HashMap
    @Json(name = "type")
    val type: Int, // 0
    @Json(name = "userId")
    val userId: Int, // 66812
    @Json(name = "visible")
    val visible: Int, // 1
    @Json(name = "zan")
    val zan: Int, // 0
)
