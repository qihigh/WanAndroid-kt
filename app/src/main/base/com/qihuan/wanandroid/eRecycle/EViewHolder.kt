/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class EViewHolder<T> :
    RecyclerView.ViewHolder, IEViewHolder<T> {

    constructor(itemView: View) : super(itemView) {
        context = itemView.context
    }

    constructor(parent: ViewGroup, @LayoutRes res: Int) : super(LayoutInflater.from(parent.context).inflate(res, parent, false)) {
        context = parent.context
    }

    protected var context: Context
    /**
     * 当前所属页面或tab
     */
    var openTab: String? = null
    private var mAdapter: IEAdapter? = null
    private val views: SparseArray<View> = SparseArray()

    /**
     * 根据id，查找对应的view
     *
     * @param id
     * @param <E>
     * @return id对应的view，优先从缓存获取
     */
    @Suppress("UNCHECKED_CAST")
    override operator fun <E : View> get(id: Int): E? {
        var childView = views[id]
        if (null == childView) {
            childView = itemView.findViewById(id)
            if (null != childView) {
                views.put(id, childView)
            }
        }
        return childView as? E
    }

    /**
     * 便捷获取textView
     *
     * @param id
     * @return
     */
    override fun getTextView(id: Int): TextView? {
        return get(id)
    }

    /**
     * 便捷获取imageView
     *
     * @param id
     * @return
     */
    override fun getImageView(id: Int): ImageView? {
        return get(id)
    }

    override fun getDataPosition(): Int =
        adapterPosition - (mAdapter?.getHeaderCount() ?: 0)

    override fun bindAdapter(adapter: IEAdapter) {
        mAdapter = adapter
    }
}

interface IEViewHolder<T> {
    fun setData(data: T)
    /**
     * 获取当前viewHolder中数据的position。会减去headerCount
     */
    fun getDataPosition(): Int

    fun getTextView(id: Int): TextView?
    fun getImageView(id: Int): ImageView?
    operator fun <E : View> get(id: Int): E?

    fun bindAdapter(adapter: IEAdapter)
}
