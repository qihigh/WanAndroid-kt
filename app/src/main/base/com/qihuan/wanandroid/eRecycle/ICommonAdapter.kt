/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.qihuan.wanandroid.R

/**
 * 封装了支持loadMore操作的基本逻辑。可以用于简化重复工作量
 */
interface ICommonAdapter : OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    /**
     * 这里封装了支持loadMore的默认实现。子类需要自行实现onLoadMore
     */
    fun wrapperAdapter(adapter: IEventDelegate) {
        adapter.setMore(
            R.layout.view_more,
            object : OnMoreListener {
                override fun onMoreShow() {
                    onLoadMore()
                }

                override fun onMoreClick() {
                    onLoadMore()
                }
            }
        )
        adapter.setErrorMore(
            R.layout.view_errormore,
            object : OnErrorListener {
                override fun onErrorClick() {
                    // 切换到loadMore，并触发loadMore
                    adapter.switch2LoadMore()
                }

                override fun onErrorShow() {
                }
            }
        )
        adapter.setNoMore(
            R.layout.view_nomore,
            object : OnNoMoreListener {
                override fun onNoMoreClick() {
                    // 切换到loadMore，并触发loadMore
                    adapter.switch2LoadMore()
                }

                override fun onNoMoreShow() {
                }
            }
        )
    }

    fun <T> commonLoad(
        adapter: EAdapter<T>,
        eRecyclerView: ERecyclerView?,
        loadEvent: LoadEvent<T>,
        itemCallback: DiffUtil.ItemCallback<T>,
        listUpdateCallback: ListUpdateCallback? = null
    ) {
        when (loadEvent.type) {
            LoadEventType.load_more_empty -> {
                eRecyclerView?.setRefreshing(false)
                // 针对loadMoreEmpty，进行快速处理，优化性能。当前大部分地方使用时，loadMoreEmpty返回的 loadEvent.data 是空，所以这里进行特殊处理
                adapter.switch2NoMore()
            }
            LoadEventType.load_new_empty,
            LoadEventType.load_new_ok,
            LoadEventType.load_more_ok,
            LoadEventType.nofity_only -> {
                eRecyclerView?.setRefreshing(false)
                adapter.submitList(
                    loadEvent.data!!,
                    loadEvent.type == LoadEventType.load_more_ok,
                    itemCallback,
                    listUpdateCallback
                )
            }
            LoadEventType.load_more_error, LoadEventType.load_more_cancel -> {
                adapter.switch2ErrorMore()
            }
            LoadEventType.load_new_error -> {
                eRecyclerView?.showError()
            }
            else -> {
                eRecyclerView?.setRefreshing(false)
            }
        }
    }
}
