/* (C)2020 */
package com.qihuan.wanandroid.app

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * 基础baseFragment
 *
 * 懒加载模式基于 viewPager的setMaxLifecycle模式，或者viewpager2+offscreenPageLimit
 */
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId), IBaseView {

    private val STATE_SAVE_IS_HIDDEN: String = "STATE_SAVE_IS_HIDDEN"

    //是否是第一次加载
    private var isFirstLoad = true;

    override fun onResume() {
        super.onResume()
        //懒加载模式
        if (isFirstLoad) {
            isFirstLoad = false;
            //仅加载一次business
            doBusiness()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fm = fragmentManager ?: return
        if (savedInstanceState != null) {
            val isSupportHidden: Boolean = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN)
            val ft: FragmentTransaction = fm.beginTransaction()
            if (isSupportHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commitAllowingStateLoss()
        }
        val bundle = arguments
        initData(bundle)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()

    }

    override fun onDestroyView() {
        LogUtil.d { "触发回收" }
        super.onDestroyView()
        isFirstLoad = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
    }

    open fun <T : View?> findViewById(@IdRes id: Int): T {
        return requireView().findViewById(id)
    }
}
