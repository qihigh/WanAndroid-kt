/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.qihuan.wanandroid.app.LogUtil
import com.qihuan.wanandroid.eRecycle.EDataObserver.Companion.OMIT_FOOTER_CHANGE

/**
 * 默认的 footerView , 用于展示 moreView、noMoreView、errorView。
 * 同时触发相关事件
 *
 * 默认 EFooterView 是最后一个条目，刷新view时直接刷新最后一条
 */
class EFooterView<T>(
    private val mAdapter: EAdapter<T>,
    private val mEventDelegate: EEventDelegate
) : ItemView {
    companion object {
        private const val HideAll = 0
        private const val ShowMore = 1
        private const val ShowError = 2
        private const val ShowNoMore = 3
    }

    private var moreView: View? = null
    private var noMoreView: View? = null
    private var errorView: View? = null
    private var moreViewRes = 0
    private var noMoreViewRes = 0
    private var errorViewRes = 0

    private var flag: Int = HideAll

    private var skipError = false
    private var skipNoMore = false

    fun setMoreView(moreView: View?) {
        this.moreView = moreView
        moreViewRes = 0
    }

    fun setNoMoreView(noMoreView: View?) {
        this.noMoreView = noMoreView
        noMoreViewRes = 0
    }

    fun setErrorView(errorView: View?) {
        this.errorView = errorView
        errorViewRes = 0
    }

    fun setMoreViewRes(moreViewRes: Int) {
        moreView = null
        this.moreViewRes = moreViewRes
    }

    fun setNoMoreViewRes(noMoreViewRes: Int) {
        noMoreView = null
        this.noMoreViewRes = noMoreViewRes
    }

    fun setErrorViewRes(errorViewRes: Int) {
        errorView = null
        this.errorViewRes = errorViewRes
    }

    override fun onCreateView(parent: ViewGroup?): View? {
        LogUtil.d { "onCreate EFooterView" }
        return parent?.let { refreshStatus(it) }
    }

    override fun hashCode(): Int {
        return flag + 13589
    }

    /**
     * 首次创建时，创建对应的view，并添加点击事件
     */
    private fun refreshStatus(parent: ViewGroup): View? {
        var view: View? = null
        when (flag) {
            ShowMore -> {
                if (moreView != null) view = moreView else if (moreViewRes != 0) view = LayoutInflater.from(parent.context).inflate(moreViewRes, parent, false)
                view?.setOnClickListener { mEventDelegate.onMoreViewClicked() }
            }
            ShowError -> {
                if (errorView != null) view = errorView else if (errorViewRes != 0) view = LayoutInflater.from(parent.context).inflate(errorViewRes, parent, false)
                view?.setOnClickListener { mEventDelegate.onErrorViewClicked() }
            }
            ShowNoMore -> {
                if (noMoreView != null) view = noMoreView else if (noMoreViewRes != 0) view = LayoutInflater.from(parent.context).inflate(noMoreViewRes, parent, false)
                view?.setOnClickListener { mEventDelegate.onNoMoreViewClicked() }
            }
        }
        if (view == null) view = FrameLayout(parent.context).apply {
//            if (BuildConfig.DEBUG) {//默认是一个没有高度的view，测试模式下，是一个宽高为0的view
//                setBackgroundColor(Color.RED)
//                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)
//            }
        }
        return view
    }

    /**
     * 当触发view渲染时，则认为 footerView 可见，则触发对应的事件。
     */
    override fun onBindView(headerView: View?) {
        headerView?.post {
            when (flag) {
                ShowMore -> mEventDelegate.onMoreViewShowed()
                ShowNoMore -> {
                    if (!skipNoMore) mEventDelegate.onNoMoreViewShowed()
                    skipNoMore = false
                }
                ShowError -> {
                    if (!skipError) mEventDelegate.onErrorViewShowed()
                    skipError = false
                }
            }
        }
    }

    /**
     * 切换view变化，默认footerView是最后一条，直接刷新
     */

    fun showError() {
        LogUtil.d { "footer showError" }
        skipError = true
        flag = ShowError
        if (mAdapter.itemCount > 0) mAdapter.notifyItemChanged(mAdapter.itemCount - 1, OMIT_FOOTER_CHANGE)
    }

    fun showMore() {
        LogUtil.d { "footer showMore" }
        flag = ShowMore
        if (mAdapter.itemCount > 0) mAdapter.notifyItemChanged(mAdapter.itemCount - 1, OMIT_FOOTER_CHANGE)
    }

    fun showNoMore() {
        LogUtil.d { "footer showNoMore" }
        skipNoMore = true
        flag = ShowNoMore
        if (mAdapter.itemCount > 0) mAdapter.notifyItemChanged(mAdapter.itemCount - 1, OMIT_FOOTER_CHANGE)
    }

    // 初始化
    fun hide() {
        LogUtil.d { "footer hide" }
        flag = HideAll
        if (mAdapter.itemCount > 0) mAdapter.notifyItemChanged(mAdapter.itemCount - 1, OMIT_FOOTER_CHANGE)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EFooterView<*>

        if (mAdapter != other.mAdapter) return false
        if (mEventDelegate != other.mEventDelegate) return false

        return true
    }
}
