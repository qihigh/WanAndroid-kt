/* (C)2020 */
package com.qihuan.wanandroid

import android.util.Log

/**
 * <p/>Created by 齐欢 on 2019/3/21.
 * 仅用于kotlin的，性能更好的日志工具。能在release版将拼装日志String消息的消耗优化掉
 */
object LogUtil {
    fun getMethodNames(sElements: Array<StackTraceElement>): Triple<String, String, Int> {
        return Triple<String, String, Int>(
            sElements[1].fileName,
            sElements[1].methodName,
            sElements[1].lineNumber
        )
    }

    inline fun d(block: () -> String) {
        if (BuildConfig.DEBUG) {
            val (className, methodName, line) = getMethodNames(Throwable().stackTrace)
            Log.d(className, String.format("[%s:%d] %s", methodName, line, block()))
        }
    }

    inline fun e(block: () -> Throwable?) {
        if (BuildConfig.DEBUG) {
            val (className, methodName, line) = getMethodNames(Throwable().stackTrace)
            Log.e(className, String.format("[%s:%d] %s", methodName, line, block()))
        }
    }

    inline fun runInDebug(yield: LogUtil.() -> Unit) {
        if (BuildConfig.DEBUG) {
            yield()
        }
    }

    fun test() {
        d { "test hahahhahaha" }
        runInDebug {
            print("display in debug")
        }
    }
}
