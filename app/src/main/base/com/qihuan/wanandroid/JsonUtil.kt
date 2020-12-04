/* (C)2020 */
package com.qihuan.wanandroid

import com.squareup.moshi.Moshi

object JsonUtil {
    val moshi: Moshi = Moshi.Builder().build()

    inline fun <reified T> toJson(vo: T): String {
        if (vo is String) {
            return vo
        }
        return moshi.adapter(T::class.java).toJson(vo)
    }

    inline fun <reified T> fromJson(vo: String): T? {
        if (T::class.java == String::class.java) {
            return vo as T
        }
        return try {
            moshi.adapter(T::class.java).fromJson(vo)
        } catch (e: Throwable) {
            LogUtil.e { e }
            null
        }
    }
}
