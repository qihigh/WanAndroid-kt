/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import android.view.View
import android.view.ViewGroup

/**
 * 用于 [EAdapter] 中的header和footer
 */
interface ItemView {
    fun onCreateView(parent: ViewGroup?): View?
    fun onBindView(headerView: View?)
}

/**
 * [EAdapter]中item的单击事件（不包含header和footer）
 */
interface OnItemClickListener {
    /**
     * 点击事件
     * @param view      触发点击的view
     * @param position  view对应的数据position（已减去headerCount）
     */
    fun onItemClick(view: View, position: Int)
}

/**
 * [EAdapter]中item的长按事件（不包含header和footer）
 */
interface OnItemLongClickListener {
    /**
     * 长按事件
     * @param view      触发点击的view
     * @param position  view对应的数据position（已减去headerCount）
     */
    fun onItemLongClick(view: View, position: Int): Boolean
}

/**
 * 触发loadMore的回调
 */
interface OnLoadMoreListener {
    /**
     * 由调用方集成，触发loadMore请求
     */
    fun onLoadMore()
}

/**
 * 用于监听loadMoreView的相关事件
 */
interface OnMoreListener {
    fun onMoreShow()
    fun onMoreClick()
}

/**
 * 用于监听NoMoreView的相关事件
 */
interface OnNoMoreListener {
    fun onNoMoreShow()
    fun onNoMoreClick()
}

interface OnErrorListener {
    fun onErrorShow()
    fun onErrorClick()
}
