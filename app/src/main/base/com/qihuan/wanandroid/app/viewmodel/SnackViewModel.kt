package com.qihuan.wanandroid.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 用于通用的存储snack msg的viewModel。通过代理的方式使用
 */
class SnackViewModel : ISnackViewModel {
    /**
     * viewModel由此触发变更
     */
    override val _snackMsg = MutableLiveData<String>().apply {
        value = ""
    }

    /**
     * activity层使用这个接收变更
     */
    override val snackMsg = _snackMsg as LiveData<String>
}

interface ISnackViewModel {
    val snackMsg: LiveData<String>

    val _snackMsg: MutableLiveData<String>
}

