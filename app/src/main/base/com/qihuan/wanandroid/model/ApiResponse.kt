/* (C)2020 */
package com.qihuan.wanandroid.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ApiResponse<T>(
    // 状态码
    val errorCode: Int,

    // 信息
    val errorMsg: String,
    // 数据
    val data: T?
) {
    val isSuccess: Boolean = errorCode == codeSuccess

    companion object {
        const val codeSuccess = 0
        const val codeError = 1
    }
}
