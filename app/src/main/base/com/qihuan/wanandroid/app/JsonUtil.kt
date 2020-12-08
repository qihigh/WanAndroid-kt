/* (C)2020 */
package com.qihuan.wanandroid.app

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type
import javax.inject.Inject

class JsonUtil @Inject constructor(val _moshi: Moshi) {

    /**
     * 转换为json结构，如果对象中包含泛型，需要通过参数 typeArguments 指明泛型类型
     */
    inline fun <reified T> toJson(vo: T, vararg typeArguments: Type): String {
        if (vo is String) {
            return vo
        }

        return _moshi.adapter<T>(
            if (typeArguments.isEmpty()) {
                T::class.java
            } else {
                Types.newParameterizedType(T::class.java, *typeArguments)
            }
        ).toJson(vo)
    }

    inline fun <reified T> fromJson(json: String, vararg typeArguments: Type): T? {
        if (T::class.java == String::class.java) {
            return json as T
        }
        return try {
            _moshi.adapter<T>(
                if (typeArguments.isEmpty()) {
                    T::class.java
                } else {
                    Types.newParameterizedType(T::class.java, *typeArguments)
                }
            ).fromJson(json)
        } catch (e: Throwable) {
            LogUtil.e { e }
            null
        }
    }
}
