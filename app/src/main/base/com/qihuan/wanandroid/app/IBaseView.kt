/* (C)2020 */
package com.qihuan.wanandroid.app

import android.os.Bundle

interface IBaseView {
    fun initData(bundle: Bundle?)
    fun initView()
    fun doBusiness()
}
