/* (C)2020 */
package com.qihuan.wanandroid.ui.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.qihuan.wanandroid.app.JsonUtil
import com.qihuan.wanandroid.app.LogUtil
import com.qihuan.wanandroid.network.RetrofitApi

class MainViewModel @ViewModelInject constructor(
    private val loginService: RetrofitApi.LoginApi,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun register() {
        loginService.register("qihigh", "123456", "123456")
            .subscribe({
                LogUtil.d { it.toString() }
            }, {
                LogUtil.e { it }
            })
    }
}
