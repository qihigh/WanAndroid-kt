/* (C)2020 */
package com.qihuan.wanandroid.app

class WException(message: String? = null, val code: Int = 0, e: Throwable? = null) :
    Exception(message, e) {
    override fun toString(): String {
        return if (code != 0) {
            "WException[$code]$message"
        } else {
            super.toString()
        }
    }
}
