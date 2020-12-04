/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.qihuan.wanandroid.BuildConfig
import com.qihuan.wanandroid.R

/**
 * 本身是FrameLayout，内部包含了RecyclerView、RefreshLayout、EmptyView。用于协调三者间关系，
 * 完成下拉刷新的支持。
 *
 * 关于刷新的 before、empty、error 的展示，由 recyclerView 控制。
 * 关于加载更多的 loading、noMore、error 的展示，由 EAdapter 控制。
 *
 * 关于header和emptyView 的展示问题，当前有3中模式:
 *
 * 1. 有header的时候不展示emptyView        easy
 * 2. 有header的时候展示emptyView         hard
 * 3. 有emptyView的时候不展示header        normal
 */
open class ERecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var mEnableForceShowRefreshing: Boolean = false
    private var mRecycler: androidx.recyclerview.widget.RecyclerView? = null

    /**
     * 当刷新后没有数据时，展示emptyView
     */
    private var mEmptyView: ViewGroup? = null

    /**
     * 刷新前的占位展示
     */
    private var mPlaceholderView: ViewGroup? = null

    /**
     * 刷新出现异常时的展示
     */
    private var mErrorView: ViewGroup? = null

    @LayoutRes
    private var mEmptyId = 0

    @LayoutRes
    private var mErrorId = 0

    @LayoutRes
    private var mPlaceholderId = 0

    private var mClipToPadding = false
    private var mPadding = 0
    private var mPaddingTop = 0
    private var mPaddingBottom = 0
    private var mPaddingLeft = 0
    private var mPaddingRight = 0
    private var mScrollbarStyle = 0
    private var mScrollbar = 0
    private var mNestScroll = false

    private var mSwpLayout: SwipeRefreshLayout? = null
    private var mRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    init {
        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.ERecyclerView)
            try {
                mClipToPadding =
                    a.getBoolean(R.styleable.ERecyclerView_eRecycler_ClipToPadding, false)
                mPadding =
                    a.getDimension(R.styleable.ERecyclerView_eRecycler_Padding, -1.0f).toInt()
                mPaddingTop =
                    a.getDimension(R.styleable.ERecyclerView_eRecycler_PaddingTop, 0.0f).toInt()
                mPaddingBottom =
                    a.getDimension(R.styleable.ERecyclerView_eRecycler_PaddingBottom, 0.0f).toInt()
                mPaddingLeft =
                    a.getDimension(R.styleable.ERecyclerView_eRecycler_PaddingLeft, 0.0f).toInt()
                mPaddingRight =
                    a.getDimension(R.styleable.ERecyclerView_eRecycler_PaddingRight, 0.0f).toInt()
                mScrollbarStyle =
                    a.getInteger(R.styleable.ERecyclerView_eRecycler_scrollbarStyle, -1)
                mScrollbar = a.getInteger(R.styleable.ERecyclerView_eRecycler_scrollbars, -1)
                mNestScroll = a.getBoolean(R.styleable.ERecyclerView_eRecycler_nestScroll, true)
                mEmptyId = a.getResourceId(R.styleable.ERecyclerView_eRecycler_layout_empty, 0)
                mErrorId = a.getResourceId(R.styleable.ERecyclerView_eRecycler_layout_error, 0)
                mPlaceholderId =
                    a.getResourceId(R.styleable.ERecyclerView_eRecycler_layout_placeholder, 0)
            } finally {
                a.recycle()
            }
        }
        initView()
    }

    private fun initView() {
        if (isInEditMode) {
            return
        }
        // 生成主View
        val v: View = LayoutInflater.from(context).inflate(R.layout.layout_erecyclerview, this)
        mSwpLayout = v.findViewById(R.id.e_swp_layout)
        mSwpLayout?.isEnabled = false

        if (!mNestScroll) {
            mSwpLayout?.isNestedScrollingEnabled = false
        }

        // 3种类型view。默认都是不可见的
        mEmptyView = v.findViewById<View>(R.id.e_empty) as ViewGroup
        mPlaceholderView = v.findViewById<View>(R.id.e_placeholder) as ViewGroup
        mErrorView = v.findViewById<View>(R.id.e_error) as ViewGroup
        // 如果xml中设置了，就进行渲染
        if (mEmptyId != 0) LayoutInflater.from(context).inflate(mEmptyId, mEmptyView)
        if (mErrorId != 0) LayoutInflater.from(context).inflate(mErrorId, mErrorView)
        if (mPlaceholderId != 0) LayoutInflater.from(context)
            .inflate(mPlaceholderId, mPlaceholderView)

        initRecyclerView(v)

//        //默认 swpLayout 仅会找当前层级view。这里
//        mSwpLayout?.setOnChildScrollUpCallback { _, child ->
//            mRecycler?.canScrollVertically(-1) ?: false
//        }
    }

    fun getSwipeToRefresh(): SwipeRefreshLayout? {
        return mSwpLayout
    }

    fun getRecyclerView(): androidx.recyclerview.widget.RecyclerView? {
        return mRecycler
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return mSwpLayout?.dispatchTouchEvent(ev) ?: false
    }

    fun setEmptyView(emptyView: View?) {
        mEmptyView?.removeAllViews()
        mEmptyView?.addView(emptyView)
    }

    fun setPlaceHolderView(placeholderView: View?) {
        mPlaceholderView?.removeAllViews()
        mPlaceholderView?.addView(placeholderView)
    }

    fun setErrorView(errorView: View?) {
        mErrorView?.removeAllViews()
        mErrorView?.addView(errorView)
    }

    /**
     * @return inflated empty view or null
     */
    fun getEmptyView(): View? =
        if (mEmptyView?.childCount ?: 0 > 0) mEmptyView?.getChildAt(0) else null

    fun getPlaceHolderView(): View? =
        if (mPlaceholderView?.childCount ?: 0 > 0) mPlaceholderView?.getChildAt(0) else null

    fun getErrorView(): View? =
        if (mErrorView?.childCount ?: 0 > 0) mErrorView?.getChildAt(0) else null

    /**
     * Implement this method to customize the AbsListView
     */
    private fun initRecyclerView(view: View) {
        mRecycler =
            view.findViewById<View>(android.R.id.list) as androidx.recyclerview.widget.RecyclerView
        setItemAnimator(null)
        if (mRecycler != null) {
            (mSwpLayout as? ESwpLayout)?.setTargetRecyclerView(mRecycler!!)
            mRecycler?.setHasFixedSize(true)
            mRecycler?.clipToPadding = mClipToPadding
            if (mPadding.toFloat() != -1.0f) {
                mRecycler?.setPadding(mPadding, mPadding, mPadding, mPadding)
            } else {
                mRecycler?.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            }
            if (mScrollbarStyle != -1) {
                mRecycler?.scrollBarStyle = mScrollbarStyle
            }
            when (mScrollbar) {
                0 -> isVerticalScrollBarEnabled = false
                1 -> isHorizontalScrollBarEnabled = false
                2 -> {
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                }
            }
        }
    }

    /**
     * 滑动到指定position
     * @param position  要滑到的position
     */
    fun scrollToPosition(position: Int) {
        // 优先使用 layoutManager 的 scroll
        (mRecycler?.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager)?.let {
            it.scrollToPositionWithOffset(position, 0)
            return
        }
        getRecyclerView()?.scrollToPosition(position)
    }

    /**
     * 滑动到指定position，并指定偏移尺寸.该模式仅支持 LinearLayoutManager . 操作成功
     * 返回true
     *
     * @param position  要滑到的position
     * @param offset    偏移量
     * @return true 操作成功 false 不支持该操作。
     */
    fun scrollToPositionWithOffset(position: Int, offset: Int): Boolean {
        (mRecycler?.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager)?.let {
            it.scrollToPositionWithOffset(position, offset)
            return true
        }
        return false
    }

    /**
     * Set the layout manager to the recycler
     *
     * @param manager
     */
    fun setLayoutManager(manager: androidx.recyclerview.widget.RecyclerView.LayoutManager?) {
        mRecycler?.layoutManager = manager
    }

    @JvmOverloads
    fun <VH : androidx.recyclerview.widget.RecyclerView.ViewHolder> setAdapter(
        adapter: androidx.recyclerview.widget.RecyclerView.Adapter<VH>,
        @IntRange(from = 0, to = 3)
        displayStrategy: Int = EDataObserver.DISPLAY_EMPTY_NO_HEADER
    ) {
        mRecycler?.adapter = adapter
        adapter.registerAdapterDataObserver(EDataObserver(this, displayStrategy))
        if (BuildConfig.DEBUG) {
            adapter.registerAdapterDataObserver(ELoggerObserver("ERecyclerView"))
        }
        (adapter as? IEAdapter)?.bindERecycler(this)
        showRecycler()
    }

    private fun hideAll() {
        mEmptyView?.visibility = View.GONE
        mPlaceholderView?.visibility = View.GONE
        mErrorView?.visibility = View.GONE
        if (!mEnableForceShowRefreshing) {
            mSwpLayout?.isRefreshing = false
        }
        mRecycler?.visibility = View.INVISIBLE
    }

    fun showEmpty() {
        mEmptyView?.let { emptyView ->
            if (emptyView.childCount > 0) {
                hideAll()
                emptyView.visibility = View.VISIBLE
                return
            }
        }
        showRecycler()
    }

    fun showError() {
        mErrorView?.let { errorView ->
            if (errorView.childCount > 0) {
                hideAll()
                errorView.visibility = View.VISIBLE
                return
            }
        }
        showRecycler()
    }

    /**
     * 展示placeHolder，应该在触发刷新前调用。后续不再调用
     */
    fun showPlaceHolder() {
        mPlaceholderView?.let { phView ->
            if (phView.childCount > 0) {
                hideAll()
                phView.visibility = View.VISIBLE
                return
            }
        }
        showRecycler()
    }

    fun showRecycler() {
        hideAll()
        mRecycler?.visibility = View.VISIBLE
    }

    fun setRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener) {
        this.mRefreshListener = listener
        mSwpLayout?.let {
            it.isEnabled = true
            it.setOnRefreshListener(listener)
        }
    }

    /**
     * 当adapter中添加了header并且启用了header和empty等同时展示的时候,会错误的触发hideAll导致刷新状态被取消,
     * 这里添加一个标识,告诉hideAll这个时候强制不取消刷新状态.
     * 当请求全部结束之后,再取消这个状态.
     * 主动触发refresh消失时,默认取消这个标识
     */
    fun setForceShowRefreshing(enable: Boolean) {
        mEnableForceShowRefreshing = enable
    }

    @JvmOverloads
    fun setRefreshing(isRefreshing: Boolean, isCallbackListener: Boolean = false) {
        mSwpLayout?.isRefreshing = isRefreshing
        if (!isRefreshing) {
            mEnableForceShowRefreshing = false
        }
        if (isRefreshing && isCallbackListener && mRefreshListener != null) {
            mRefreshListener?.onRefresh()
        }
    }

    /*trend_video*
     * Add scroll listener to the recycler
     *
     * @param listener
     */
    fun addOnScrollListener(listener: androidx.recyclerview.widget.RecyclerView.OnScrollListener) {
        mRecycler?.addOnScrollListener(listener)
    }

    /**
     * Remove scroll listener from the recycler
     *
     * @param listener
     */
    fun removeOnScrollListener(listener: androidx.recyclerview.widget.RecyclerView.OnScrollListener) {
        mRecycler?.removeOnScrollListener(listener)
    }

    fun clearOnScrollListener() {
        mRecycler?.clearOnScrollListeners()
    }

    /**
     * @return the recycler adapter
     */
    fun getAdapter(): androidx.recyclerview.widget.RecyclerView.Adapter<*>? {
        return mRecycler?.adapter
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setOnTouchListener(listener: OnTouchListener?) {
        mRecycler?.setOnTouchListener(listener)
    }

    fun setItemAnimator(animator: ItemAnimator?) {
        mRecycler?.itemAnimator = animator
    }

    fun addItemDecoration(itemDecoration: ItemDecoration) {
        mRecycler?.addItemDecoration(itemDecoration)
    }

    fun addItemDecoration(itemDecoration: ItemDecoration, index: Int) {
        mRecycler?.addItemDecoration(itemDecoration, index)
    }

    fun removeItemDecoration(itemDecoration: ItemDecoration) {
        mRecycler?.removeItemDecoration(itemDecoration)
    }

    fun showEmptyAndHeader() {
        showRecycler()
        (mRecycler?.adapter as? EAdapter<*>)?.switch2hide()
        mEmptyView?.takeIf { it.childCount > 0 }?.visibility = View.VISIBLE
    }
}
