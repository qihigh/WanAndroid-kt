/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.qihuan.wanandroid.BuildConfig
import com.qihuan.wanandroid.LogUtil
import java.lang.ref.WeakReference
import kotlin.IllegalArgumentException

/**
 * 和 [ERecyclerView] 配合使用，支持header、footer。
 *
 * 更新数据通过 DiffUtil
 *
 * 针对国际版情况,优化后的adapter,原理基本和[RecyclerArrayAdapter]一致,
 * 但是调整了一些不适用的功能,并限定了一些逻辑.避免踩一些坑.
 * 同时添加了更方便实现 header 和 内容empty 时共存的逻辑.
 *
 * @param mContext  context
 * @param data      初始化时使用的数据，可以为空。实际使用时会新建一个ArrayList承载
 */
abstract class EAdapter<T> @JvmOverloads constructor(
    private val mContext: Context,
    private val data: List<T>? = null,
    /**
     * 事件代理对象，代理了 loadMore 的相关处理、
     *
     * 默认实现是[EEventDelegate]，内置了默认的 [EFooterView]
     */
    private val mEventDelegate: IEventDelegate = EEventDelegate()
) : RecyclerView.Adapter<EViewHolder<T>>(),
    IEAdapter,
    IEventDelegate by mEventDelegate {

    private var mRecyclerViewRef: WeakReference<ERecyclerView?> = WeakReference(null)

    /**
     * 实际adapter的数据
     */
    private var mObjects: List<T> = ArrayList()

    private var headers: MutableList<ItemView> = ArrayList()
    private var footers: MutableList<ItemView> = ArrayList()

    open var itemClickListener: OnItemClickListener? = null
    open var itemLongClickListener: OnItemLongClickListener? = null

    /**
     * 仅在插入数据时生效。当插入的数据位于最前面是，是否自动滚动到顶部。（绝大部分情况是需要的。
     * 让用户立刻感知到最新数据。逆序模式需要关闭。不是插入数据的情况无所谓）
     *
     *
     * 当数据不足一屏时，position 0 插入都会自动滑动到顶部。
     */
    open var autoScrollTop: Boolean = true

    init {
        if (!data.isNullOrEmpty()) {
            mObjects = data.toList()
        }
        @Suppress("LeakingThis")
        mEventDelegate.bindAdapter(this)
    }

    fun getContext() = mContext

    /**
     * 覆盖RecyclerView.Adapter方法，不允许再次覆盖。
     *
     * 实现多viewType，重写[getViewType]方法
     */
    final override fun getItemViewType(unusedPosition: Int): Int {
        if (headers.isNotEmpty() && unusedPosition < headers.size) {
            return headers[unusedPosition].hashCode()
        }
        val i = unusedPosition - headers.size - getCount()
        if (footers.isNotEmpty() && i >= 0) {
            return footers[i].hashCode()
        }

        return getViewType(unusedPosition - headers.size)
    }

    /**
     * 如果是多viewType的类型，则覆盖该方法
     * @param position item位置（已减去header)
     * @return viewType 默认是0
     */
    open fun getViewType(position: Int): Int {
        return 0
    }

    /**
     * 基本的实现，新建view，并进行基本的绑定
     *
     * 覆盖RecyclerView.Adapter方法，不允许再次覆盖
     */
    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EViewHolder<T> {
        // 根据viewType，优先确定是否是header、footer
        (headers + footers).find { it.hashCode() == viewType }?.let { itemView ->
            itemView.onCreateView(parent)?.let {
                return StateViewHolder(it)
            }
        }

        val viewHolder: EViewHolder<T> = eCreateViewHolder(parent, viewType)
        // viewHolder.itemViewType
        viewHolder.bindAdapter(this)

        itemClickListener?.let { listener ->
            viewHolder.itemView.setOnClickListener {
                val position = viewHolder.getDataPosition()
                if (position >= 0 && position < getCount()) {
                    listener.onItemClick(it, position)
                }
            }
        }

        itemLongClickListener?.let { listener ->
            viewHolder.itemView.setOnLongClickListener {
                val position = viewHolder.getDataPosition()
                return@setOnLongClickListener if (position >= 0 && position < getCount()) {
                    listener.onItemLongClick(it, position)
                } else {
                    false
                }
            }
        }

        // val ss : TextView? = viewHolder[R.id.RelativeLayout1]

        return viewHolder
    }

    /**
     * 负责生成viewHolder， [EViewHolder]
     *
     * 1. 包含了 itemView 的初始化
     *
     * 2. 可以用于获取 itemView 中的各个子view，并自带缓存
     *
     * 3. 用于获取 adapterPosition （已减去headerCount）
     */
    abstract fun eCreateViewHolder(parent: ViewGroup, viewType: Int): EViewHolder<T>/* {
        return object : EViewHolder<T>(parent, 0) {
            override fun setData(data: T) {
            }

        }
    }*/

    /**
     * 覆盖RecyclerView.Adapter方法，不允许再次覆盖
     * 获取条目数量，应当使用 [getCount] 方法
     */
    @Deprecated("获取条目数量，应当使用 [getCount] 方法")
    final override fun getItemCount(): Int = headers.size + getCount() + footers.size

    /**
     * 覆盖RecyclerView.Adapter方法，不允许再次覆盖
     *
     * 不包含payload的时候触发的绑定.
     *
     * 1. footer和header独立处理
     *
     * 2. 内容部分调用[eBindViewHolder]进行处理。内部实际是调用[EViewHolder]的
     *  [EViewHolder#setData]方法进行处理。一般情况下不需要重写该方法。
     */
    final override fun onBindViewHolder(holder: EViewHolder<T>, unusedPosition: Int) {
        if (headers.isNotEmpty() && unusedPosition < headers.size) {
            headers[unusedPosition].onBindView(holder.itemView)
            return
        }
        val i = unusedPosition - headers.size - getCount()
        if (footers.isNotEmpty() && i >= 0) {
            footers[i].onBindView(holder.itemView)
            return
        }

        eBindViewHolder(holder)
    }

    /**
     * `覆盖RecyclerView.Adapter方法，不允许再次覆盖`
     *
     * 包含payload的绑定
     *
     * 1.如果是header或者footer，处理方式和不含payload的方式一样（这里暂不支持header、footer 通过payload 更新，觉得没必要） <br/>
     *
     * 2.如果payload为空，调用默认父类实现，即忽略payload，调用 [onBindViewHolder] <br/>
     *
     * 3.如果payload不为空，判断是否拦截`bindView`,是否拦截由[interruptBindViewHolder]控制. <br/>
     */
    final override fun onBindViewHolder(
        holder: EViewHolder<T>,
        unusedPosition: Int,
        payloads: MutableList<Any>
    ) {
        // 同onBindViewHolder,处理header和footer
        if (headers.isNotEmpty() && unusedPosition < headers.size) {
            super.onBindViewHolder(holder, unusedPosition, payloads)
            return
        }
        val i = unusedPosition - headers.size - getCount()
        if (footers.isNotEmpty() && i >= 0) {
            super.onBindViewHolder(holder, unusedPosition, payloads)
            return
        }

        if (payloads.isEmpty() || !interruptBindViewHolder(holder, payloads)) {
            // 父类默认实现，调用的不包含payload的绑定
            super.onBindViewHolder(holder, unusedPosition, payloads)
        }
    }

    open fun eBindViewHolder(holder: EViewHolder<T>) {
        // 填充实际的数据
        holder.setData(getItem(holder.getDataPosition()))
    }

    /**
     * 是否拦截bindViewHolder，默认是false不拦截。
     *
     * 子类可覆盖该方法，在内部处理payLoad的各种变化，来实现payLoad方式的更新；默认实现是忽略payLoad
     * @return true，不再调用 bindViewHolder ; false 仍调用bindViewHolder，忽略 payLoad
     */
    open fun <T> interruptBindViewHolder(
        holder: EViewHolder<T>,
        payloads: MutableList<Any>
    ): Boolean {

        return false
    }

    override fun addHeader(view: ItemView, pos: Int?) {
        addHeaders(listOf(view), pos)
    }

    override fun addHeaders(views: List<ItemView>, pos: Int?) {
        if (views.isEmpty()) return
        val posIndex = when {
            (null == pos || pos >= headers.size) -> headers.size
            pos < 0 -> 0
            else -> pos
        }
        headers.addAll(posIndex, views)
        // 立即刷新UI
        notifyItemRangeInserted(posIndex, views.size)
        scroll2TopIfNeed(posIndex)
    }

    override fun addFooter(view: ItemView, pos: Int?) {
        addFooters(listOf(view), pos)
    }

    override fun addFooters(views: List<ItemView>, pos: Int?) {
        if (views.isEmpty()) return
        val posIndex = when {
            (null == pos || pos >= footers.size) -> {
                if (views.last() !is EFooterView<*>) {
                    LogUtil.d { "注意：如果footer添加到了末尾，会影响默认FooterView的刷新；如果必须如此，那么需要更换默认FooterView的实现" }
                }
                footers.size
            }
            pos < 0 -> 0
            else -> pos
        }
        footers.addAll(posIndex, views)
        // 立即刷新UI
        notifyItemRangeInserted(headers.size + getCount() + posIndex, views.size)
    }

    final override fun getCount() = mObjects.size

    override fun bindERecycler(eRecyclerView: ERecyclerView) {
        mRecyclerViewRef = WeakReference(eRecyclerView)
    }

    override fun removeAllHeader() {
        if (headers.isEmpty()) return
        val count = headers.size
        headers.clear()
        notifyItemRangeRemoved(0, count)
    }

    override fun removeAllFooter() {
        if (footers.isEmpty()) return
        val count = footers.size
        footers.clear()
        notifyItemRangeRemoved(headers.size + getCount(), count)
    }

    override fun removeHeader(view: ItemView) {
        val index = headers.indexOf(view)
        if (index != -1) {
            headers.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun removeFooter(view: ItemView) {
        val index = footers.indexOf(view)
        if (index != -1) {
            footers.removeAt(index)
            notifyItemRemoved(headers.size + getCount() + index)
        }
    }

    override fun getHeader(index: Int): ItemView? = headers.getOrNull(index)

    override fun getFooter(index: Int): ItemView? = footers.getOrNull(index)

    override fun getHeaderCount(): Int = headers.size

    override fun getFooterCount(): Int = footers.size

    /**
     * 获取条目
     */
    fun getItem(position: Int): T {
        return mObjects[position]
    }

    /**
     * 获取所有条目
     */
    fun getItems(): List<T> = mObjects.toList()

    fun setItemsWithDiff(data: List<T>) {
        this.mObjects = data.toMutableList()
    }

    /**
     * 快速操作，清除全部数据
     */
    fun clear() {
        submitList(
            emptyList(),
            isLoadMore = false,
            itemCallback = object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = true

                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = true
            }
        )
    }

    /**
     * 快速操作，移除条目
     */
    fun removeAt(position: Int, itemCallback: DiffUtil.ItemCallback<T>) {
        if (position < 0 || position > getCount()) return
        submitList(
            getItems().toMutableList().apply { removeAt(position) },
            isLoadMore = false,
            itemCallback = itemCallback
        )
    }

    /**
     * 快速操作，更新条目（由于使用DiffUtil，新老条目必须不能是同一个对象）
     */
    @Throws(IllegalArgumentException::class)
    fun updateAt(position: Int, item: T, itemCallback: DiffUtil.ItemCallback<T>) {
        if (position < 0 || position > getCount()) return
        // 找到目标条目
        val destItem = getItem(position)
        // 进行校验，diffUtil方式，不允许新条目和老条目是同一个对象。
        if (destItem === item) {
            if (BuildConfig.DEBUG) {
                throw IllegalArgumentException("新老条目相同，diffUtil无法判断出是该条目发生了变化")
            }
            return
        }

        submitList(
            getItems().toMutableList().apply { set(position, item) },
            isLoadMore = false,
            itemCallback = itemCallback
        )
    }

    /**
     * 针对复杂对象，不适用DiffUtil，强制进行更新(因为更新条目不需要更新 loadMoreView 和 emptyView ，所以是可行的)
     *
     * 可以支持新老对象是同一个对象
     *
     * @param position      要更新的position
     * @param item          要更新的条目，允许和目标item是同一个对象
     * @param payloads      payload更新模式
     */
    @JvmOverloads
    fun forceUpdateAt(position: Int, item: T, payloads: Any? = null) {
        if (position < 0 || position > getCount()) return
        if (item === getItem(position)) {
            LogUtil.d { "新老对象是同一个，无需更新" }
        } else {
            mObjects = getItems().toMutableList().apply { set(position, item) }
        }
        // 刷新 view
        notifyItemChanged(position + headers.size, payloads)
    }

    /**
     * 提交了新的数据，和老数据进行 DiffUtil 对比并更新。
     *
     * 需要区分是否是loadMore，因为loadMore需要更新 loadMoreView。
     *
     * 当loadMore发生异常的时候，不应该调用该方法进行更新(一般来说，error情况下数据集也无变化，不需要diff)。而是主动切换道 errorMoreView。
     * 这里仅处理 loadMoreOK 和 noMore。
     *
     * 当loadNew的时候，如果数据量增加或者不变，不需要处理 loadMoreView，而当数据量减少时，
     * 如果当前 loadMoreView 恰好是 noMoreView，会出现无法自动触发loadMore的情况，这个时候应当主动切换到
     * loadMoreView,在展示时自动触发loadMore。
     *
     * 支持payload更新，itemCallback覆盖 [DiffUtil.ItemCallback#getChangePayload] 方法
     *
     * @param newList           最新要展示的数据，会和当前展示的数据进行diff
     * @param isLoadMore        loadMore情况下，需要对 moreView 进行处理
     * @param itemCallback      用于比较条目是否相同。
     * @param updateCallback    更新回调，可以为null。会使用默认的回调。（tips:自定义情况下，需要自行添加 headers.size 的位移）
     */
    @Throws(IllegalArgumentException::class)
    fun submitList(
        newList: List<T>,
        isLoadMore: Boolean = false,
        itemCallback: DiffUtil.ItemCallback<T>,
        updateCallback: ListUpdateCallback? = null
    ) {
        val readOnlyList = getItems()
        if (readOnlyList === newList && newList.isNotEmpty()) {
            // nothing to do
            if (BuildConfig.DEBUG) {
                throw IllegalArgumentException("新老数据不能是同一个对象，无法适用DiffUtil")
            }
            LogUtil.d { "新老数据是同一个对象，抛出异常" }
            return
        }

        // ListUpdateCallback 中需要加上 headerCount 进行view更新
        val mUpdateCallback = updateCallback ?: EListUpdateCallback(this)
        if (newList.isEmpty()) {
            // fast remove all
            val countRemove = readOnlyList.size
            mObjects = emptyList()
            // 隐藏loadMore
            switch2hide()
            // 不包含header . dataObserver会控制切换到emptyView
            mUpdateCallback.onRemoved(0 + headers.size, countRemove)
            return
        }

        if (readOnlyList.isEmpty()) {
            // fast add all
            mObjects = mObjects + newList
            // 启用loadMore
            switch2LoadMore()
            // 不包含header . dataObserver会控制切换到RecyclerView
            mUpdateCallback.onInserted(0 + headers.size, getCount())
            return
        }

        // 新或老数据为空的情况，不应该会触发loadMore，所以在这里处理loadMore
        if (isLoadMore) {
            /**
             * 1. loadMore 加载成功后，添加数据，通知 loadMoreView 刷新。（数据不满足一屏时，会再次触发loadMore）
             *
             * 2. loadMore 加载成功后，数据为空，切换到 noMoreView。
             *
             * 3. loadMore 加载失败，切换到 errorMoreView (是发生异常时主动触发，不在这里处理)
             */
            if (newList.size <= readOnlyList.size) {
                // noMore
                switch2NoMore()
            } else {
                // 恢复被消费的 isLoadingMore 标识，允许onMoreViewShown时再次触发loadMore
                switch2LoadMore()
            }
        } else {
            //
            switch2LoadMore()
        }

        // 使用diffUtil
        val diffResult = DiffUtil.calculateDiff(
            EDiffCallback(readOnlyList, newList, itemCallback)
        )

        mObjects = newList.toList()
        diffResult.dispatchUpdatesTo(mUpdateCallback)
    }

    /**
     * 确定是否需要滑动到顶部
     */
    fun scroll2TopIfNeed(posIndex: Int) {
        if (posIndex == 0 && autoScrollTop) {
            // 插入header时，默认滑动到顶部 （该行为可以被禁掉）
            mRecyclerViewRef.get()?.scrollToPositionWithOffset(0, 0)
        }
    }
}

interface IEAdapter {
    /**
     * 添加header
     * @param view  headerView
     * @param pos   添加的起始位置，默认为最后一个
     */
    fun addHeader(view: ItemView, pos: Int? = null)

    /**
     * 添加header
     * @param views  headerView
     * @param pos   添加的起始位置，默认为最后一个
     */
    fun addHeaders(views: List<ItemView>, pos: Int? = null)

    fun addFooter(view: ItemView, pos: Int? = null)

    fun addFooters(views: List<ItemView>, pos: Int? = null)

    fun removeAllHeader()

    fun removeAllFooter()

    fun removeHeader(view: ItemView)
    fun removeFooter(view: ItemView)
    fun getHeader(index: Int): ItemView?
    fun getFooter(index: Int): ItemView?

    fun getHeaderCount(): Int
    fun getFooterCount(): Int

    /**
     * 获取不包含header、footer的size
     */
    fun getCount(): Int

    /**
     * adapter绑定当前recyclerView
     */
    fun bindERecycler(eRecyclerView: ERecyclerView)
}

/**
 * 用于承载header、footer的viewHolder
 */
class StateViewHolder<T>(itemView: View) : EViewHolder<T>(itemView) {
    override fun setData(data: T) {
    }
}

/**
 * 默认的listUpdateCallback
 */
open class EListUpdateCallback(private val adapter: EAdapter<*>) : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(position + adapter.getHeaderCount(), count, payload)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) { // 确认该逻辑是否正确（目前用到的地方：私信列表页）
        adapter.notifyItemMoved(
            fromPosition + adapter.getHeaderCount(),
            toPosition + adapter.getHeaderCount()
        )
    }

    override fun onInserted(position: Int, count: Int) {
        adapter.scroll2TopIfNeed(position)
        adapter.notifyItemRangeInserted(position + adapter.getHeaderCount(), count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(position + adapter.getHeaderCount(), count)
    }
}
