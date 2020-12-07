/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import android.view.View
import androidx.annotation.LayoutRes
import com.qihuan.wanandroid.app.LogUtil

/**
 * 默认的eventDelegate。控制loadMore的相关事件，包括loadMore、NoMore、loadMoreError。
 *
 * 内部包含一个footerView默认实现，会作为 adapter 的默认footer。默认footerView是最后一个footer
 */
class EEventDelegate : IEventDelegate {

    private lateinit var mAdapter: IEAdapter
    private lateinit var footerView: EFooterView<*>

    private var onMoreListener: OnMoreListener? = null
    private var onNoMoreListener: OnNoMoreListener? = null
    private var onErrorListener: OnErrorListener? = null

    private var hasData = false
    private var isLoadingMore = false

    private var hasMore = false
    private var hasNoMore = false
    private var hasError = false

    private val STATUS_INITIAL = 291
    private val STATUS_MORE = 260
    private val STATUS_NOMORE = 408
    private val STATUS_ERROR = 732

    /**
     * 当前状态
     */
    private var status = STATUS_INITIAL

    override fun bindAdapter(adapter: EAdapter<*>) {
        mAdapter = adapter
        footerView = EFooterView(adapter, this)
        // 添加footerView
        adapter.addFooter(footerView)
    }

    fun onMoreViewShowed() {
        LogUtil.d { "onMoreViewShowed" }
        if (!isLoadingMore && onMoreListener != null) {
            isLoadingMore = true
            onMoreListener?.onMoreShow()
        }
    }

    /**
     * 代理相关监听
     */

    fun onMoreViewClicked() {
        onMoreListener?.onMoreClick()
    }

    fun onErrorViewShowed() {
        onErrorListener?.onErrorShow()
    }

    fun onErrorViewClicked() {
        onErrorListener?.onErrorClick()
    }

    fun onNoMoreViewShowed() {
        onNoMoreListener?.onNoMoreShow()
    }

    fun onNoMoreViewClicked() {
        onNoMoreListener?.onNoMoreClick()
    }

    override fun switch2NoMore() {
        LogUtil.d { "stopLoadMore" }
        footerView.showNoMore()
        status = STATUS_NOMORE
        isLoadingMore = false
    }

    override fun switch2ErrorMore() {
        LogUtil.d { "pauseLoadMore" }
        footerView.showError()
        status = STATUS_ERROR
        isLoadingMore = false
    }

    override fun switch2LoadMore() {
        isLoadingMore = false
        footerView.showMore()
        status = STATUS_MORE
    }

    override fun resumeLoadMore() {
        switch2LoadMore()
        onMoreViewShowed()
    }

    override fun switch2hide() {
        LogUtil.d { "clear" }
        hasData = false
        status = STATUS_INITIAL
        footerView.hide()
        isLoadingMore = false
    }

    /**
     * 设置3种view以及对应监听
     */

    override fun setMore(view: View?, listener: OnMoreListener?) {
        footerView.setMoreView(view)
        onSetMore(listener)
    }

    override fun setMore(@LayoutRes res: Int, listener: OnMoreListener?) {
        footerView.setMoreViewRes(res)
        onSetMore(listener)
    }

    private fun onSetMore(listener: OnMoreListener?) {
        onMoreListener = listener
        hasMore = true
        // 为了处理setMore之前就添加了数据的情况
        if (mAdapter.getCount() > 0) {
            footerView.showMore()
        }
    }

    override fun setNoMore(view: View?, listener: OnNoMoreListener?) {
        footerView.setNoMoreView(view)
        onSetNoMore(listener)
    }

    override fun setNoMore(@LayoutRes res: Int, listener: OnNoMoreListener?) {
        footerView.setNoMoreViewRes(res)
        onSetNoMore(listener)
    }

    private fun onSetNoMore(listener: OnNoMoreListener?) {
        onNoMoreListener = listener
        hasNoMore = true
    }

    override fun setErrorMore(view: View?, listener: OnErrorListener?) {
        footerView.setErrorView(view)
        onSetError(listener)
    }

    override fun setErrorMore(@LayoutRes res: Int, listener: OnErrorListener?) {
        footerView.setErrorViewRes(res)
        onSetError(listener)
    }

    private fun onSetError(listener: OnErrorListener?) {
        onErrorListener = listener
        hasError = true
    }
}

interface IEventDelegate {
    /**
     * 停止loadMore，显示noMore
     */
    fun switch2NoMore()

    /**
     * 停止loadMore，显示error
     */
    fun switch2ErrorMore()

    /**
     * 隐藏所有，footerView 不展示
     */
    fun switch2hide()

    /**切换到loadMore
     *
     */
    fun switch2LoadMore()

    /**
     * 重新loadMore
     *
     * 切换到loadMore，并触发loadMore
     *
     * tips:内部实现是调用 switch2LoadMore , 然后主动触发一下 onMoreViewShow。
     * 但其实正常情况下，switch2LoadMore 会触发 notifyItemChange。会触发 bindView 的调用。
     * 进一步也会触发 onMoreViewShow。这里添加这个方法仅作为备用，备用于[switch2LoadMore]未能触发 onMoreViewShow 的情况
     */
    @Deprecated("优先使用 switch2LoadMore , 如果发现确实会有不触发loadMore 的情况，再考虑使用该方法")
    fun resumeLoadMore()

    fun setMore(view: View?, listener: OnMoreListener?)
    fun setNoMore(view: View?, listener: OnNoMoreListener?)
    fun setErrorMore(view: View?, listener: OnErrorListener?)
    fun setMore(@LayoutRes res: Int, listener: OnMoreListener?)
    fun setNoMore(@LayoutRes res: Int, listener: OnNoMoreListener?)
    fun setErrorMore(@LayoutRes res: Int, listener: OnErrorListener?)
    fun bindAdapter(adapter: EAdapter<*>)
}
