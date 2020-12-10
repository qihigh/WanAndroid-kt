/* (C)2020 */
package com.qihuan.wanandroid.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Project(
    @Json(name = "apkLink")
    val apkLink: String,
    @Json(name = "audit")
    val audit: Int, // 1
    @Json(name = "author")
    val author: String, // venshine
    @Json(name = "canEdit")
    val canEdit: Boolean, // false
    @Json(name = "chapterId")
    val chapterId: Int, // 539
    @Json(name = "chapterName")
    val chapterName: String, // 未分类
    @Json(name = "collect")
    val collect: Boolean, // false
    @Json(name = "courseId")
    val courseId: Int, // 13
    @Json(name = "desc")
    val desc: String, // 通过de Casteljau算法绘制贝塞尔曲线，并计算它的切线，实现1-7阶贝塞尔曲线的形成动画。
    @Json(name = "descMd")
    val descMd: String,
    @Json(name = "envelopePic")
    val envelopePic: String, // https://www.wanandroid.com/blogimgs/a128f17e-5363-42ec-9240-cb335a0b8b29.png
    @Json(name = "fresh")
    val fresh: Boolean, // true
    @Json(name = "id")
    val id: Int, // 16395
    @Json(name = "link")
    val link: String, // https://www.wanandroid.com/blog/show/2824
    @Json(name = "niceDate")
    val niceDate: String, // 18小时前
    @Json(name = "niceShareDate")
    val niceShareDate: String, // 18小时前
    @Json(name = "origin")
    val origin: String,
    @Json(name = "prefix")
    val prefix: String,
    @Json(name = "projectLink")
    val projectLink: String, // https://github.com/venshine/BezierMaker
    @Json(name = "publishTime")
    val publishTime: Long, // 1607531066000
    @Json(name = "realSuperChapterId")
    val realSuperChapterId: Int, // 293
    @Json(name = "selfVisible")
    val selfVisible: Int, // 0
    @Json(name = "shareDate")
    val shareDate: Long, // 1607531066000
    @Json(name = "shareUser")
    val shareUser: String,
    @Json(name = "superChapterId")
    val superChapterId: Int, // 294
    @Json(name = "superChapterName")
    val superChapterName: String, // 开源项目主Tab
    @Json(name = "tags")
    val tags: List<ProjectTag>,
    @Json(name = "title")
    val title: String, // 通过de Casteljau算法绘制贝塞尔曲线，并计算它的切线，实现1-7阶贝塞尔曲线的形成动画。
    @Json(name = "type")
    val type: Int, // 0
    @Json(name = "userId")
    val userId: Int, // -1
    @Json(name = "visible")
    val visible: Int, // 1
    @Json(name = "zan")
    val zan: Int // 0
)
