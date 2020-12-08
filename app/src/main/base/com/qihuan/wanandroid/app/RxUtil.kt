package com.qihuan.wanandroid.app

import com.qihuan.wanandroid.model.ApiResponse
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 快速切换，执行在子线程，配合compose使用
 */
fun <Upstream> applyAsync(): ObservableTransformer<Upstream, Upstream> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
    }
}

/**
 * 快速切换，执行在子线程，最终切换到UI线程，配合compose使用
 */
fun <Upstream> applyUIAsync(): ObservableTransformer<Upstream, Upstream> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

/**
 * 分离异常分支
 */
fun <Downstream> applyResponseTransform(): ObservableTransformer<ApiResponse<Downstream>, ApiResponse<Downstream>> {
    return ObservableTransformer { upstream ->
        upstream.doOnNext { apiResponse ->
            if (!apiResponse.isSuccess) {
                throw WException(apiResponse.errorMsg, apiResponse.errorCode)
            }
        }
    }

}