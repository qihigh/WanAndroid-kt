package com.qihuan.wanandroid.app

import androidx.fragment.app.Fragment
import com.qihuan.wanandroid.WApplication

open class BaseFragment : Fragment() {
    protected val appComponent: AppComponent
        get() = (WApplication.context as WApplication).getAppComponent()
}