/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import android.util.Log
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.qihuan.wanandroid.app.LogUtil

/**
 * 监听数据集合的变化，决定是否展示 emptyView
 *
 * 需要注意的是，[EAdapter]中 footer 的变化，无需触发 change。因为他存在与否不影响是否展示空页面。
 * 这里的优化办法是，footer均通过 payload 进行change提示，这里捕捉到之后，进行忽略
 *
 * @param recyclerView      绑定的recyclerView，自动控制其view的切换
 * @param displayStrategy   展示策略，针对极端情况（有header并且数据为空的情况），处理header和emptyView的展示模式
 */
class EDataObserver @JvmOverloads constructor(
    private val recyclerView: ERecyclerView,
    private val displayStrategy: Int = DISPLAY_EMPTY_NO_HEADER
) : AdapterDataObserver() {

    companion object {
        /**
         * empty 优先级高于 header。当有header并且没有数据的时候，不展示header
         *
         * 默认
         */
        const val DISPLAY_EMPTY_NO_HEADER = 0

        /**
         * empty 和 header 可以共存 (empty 位于 header 背后 )
         */
        const val DISPLAY_EMPTY_AND_HEADER = 1

        /**
         * header 优先级高于 empty。当有header并且没有数据的时候，展示header,不展示empty
         */
        const val DISPLAY_HEADER_NO_EMPTY = 2

        /**
         * 当前observer忽略footerView的变化。可以提升部分性能
         */
        const val OMIT_FOOTER_CHANGE = "omit_footer_change"
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        onChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        if (payload == OMIT_FOOTER_CHANGE) {
            LogUtil.d { OMIT_FOOTER_CHANGE }
            return
        }
        onChanged()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        onChanged()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        onChanged()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        onChanged()
    }

    @CallSuper
    override fun onChanged() {
        super.onChanged()
        val adapter = recyclerView.getAdapter() ?: return
        var count: Int
        if (adapter is EAdapter<*>) {
            count = adapter.getCount()
            if (adapter.getHeaderCount() != 0 && count == 0) {
                // 针对有header的情况进行特殊处理
                when (displayStrategy) {
                    DISPLAY_EMPTY_NO_HEADER -> {
                        LogUtil.d { "默认情况，继续后续处理" }
                    }
                    DISPLAY_HEADER_NO_EMPTY -> {
                        LogUtil.d { "计算count时将header计算进去" }
                        count += adapter.getHeaderCount()
                    }
                    DISPLAY_EMPTY_AND_HEADER -> {
                        // 特殊的
                        LogUtil.d { "同时展示empty和header" }
                        recyclerView.showEmptyAndHeader()
                        return
                    }
                }
            }
        } else {
            count = adapter.itemCount
        }
        if (count == 0) {
            LogUtil.d { "no data show empty" }
            recyclerView.showEmpty()
        } else {
            LogUtil.d { "has data show recycler" }
            recyclerView.showRecycler()
        }
    }
}

/**
 * 打日志专用
 *
 * @param tag   打log的tag
 */
class ELoggerObserver(private val tag: String) : AdapterDataObserver() {
    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        Log.d(tag, "Item range changed. Start: $positionStart Count: $itemCount")
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        if (payload == null) {
            onItemRangeChanged(positionStart, itemCount)
        } else {
            Log.d(
                tag,
                "Item range changed with payloads. Start: $positionStart Count: $itemCount"
            )
        }
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        Log.d(tag, "Item range inserted. Start: $positionStart Count: $itemCount")
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        Log.d(tag, "Item range removed. Start: $positionStart Count: $itemCount")
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        Log.d(tag, "Item moved. From: $fromPosition To: $toPosition")
    }
}
