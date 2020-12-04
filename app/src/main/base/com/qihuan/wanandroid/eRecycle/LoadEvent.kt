/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

class LoadEvent<T>(
    val type: LoadEventType? = null,
    var data: List<T>? = null,
    var errorMsg: String? = null
)

/**
 * 加载消息
 */
enum class LoadEventType {
    load_new_ok,
    load_new_empty,
    load_new_error,
    load_more_ok,
    load_more_error,
    load_more_empty,
    nofity_only,
    load_more_cancel,
    load_new_cancel // 用于过于频繁的请求，取消当前请求
}
