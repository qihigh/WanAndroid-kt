/* (C)2020 */
package com.qihuan.wanandroid.app

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId), IBaseView {

    private val STATE_SAVE_IS_HIDDEN: String = "STATE_SAVE_IS_HIDDEN"

    protected var mIsVisibleToUser = false
    protected var mIsBusinessDone = false
    protected var mIsInPager = false

    /**
     * @return true true [.doBusiness] will lazy in view pager, false otherwise
     */
    open fun isLazy(): Boolean {
        return false
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsInPager = true
        if (isVisibleToUser) mIsVisibleToUser = true
        if (isLazy()) {
            if (!mIsBusinessDone && isVisibleToUser && view != null) {
                mIsBusinessDone = true
                doBusiness()
            }
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
        if (!mIsInPager || !isLazy() || mIsVisibleToUser) {
            mIsBusinessDone = true
            doBusiness()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsVisibleToUser = false
        mIsBusinessDone = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
    }

    open fun <T : View?> findViewById(@IdRes id: Int): T {
        return requireView().findViewById(id)
    }
}
