/* (C)2020 */
package com.qihuan.wanandroid.eRecycle

import androidx.recyclerview.widget.DiffUtil

/**
 * 简单封装DiffCallback。提出通用逻辑，自行实现 diffCallback ， 来进行item的比较
 */
class EDiffCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val diffCallback: DiffUtil.ItemCallback<T>
) : DiffUtil.Callback() {

    companion object {
        /**
         * 使用kotlin代理封装一层 itemCallback.
         * payLoad模式,需要自行实现 ItemCallback
         */
        @JvmStatic
        fun <T> diffItemCallbackHelper(
            /**
             * 判断是同一个条目
             */
            areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
            /**
             * 判断同一个条目，内容是否有变化
             */
            areContentsTheSame: ((oldItem: T, newItem: T) -> Boolean)? = null,
            /**
             * payload mode
             */
            tryGetChangePayload: ((oldItem: T, newItem: T) -> Any?)? = null
        ): DiffUtil.ItemCallback<T> {
            return object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areItemsTheSame(oldItem, newItem)

                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areContentsTheSame?.invoke(oldItem, newItem)
                    ?: true

                override fun getChangePayload(oldItem: T, newItem: T): Any? = tryGetChangePayload?.invoke(oldItem, newItem)
                    ?: super.getChangePayload(oldItem, newItem)
            }
        }
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return diffCallback.areItemsTheSame(
            oldList[oldItemPosition],
            newList[newItemPosition]
        )
    }

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return diffCallback.areContentsTheSame(
            oldList[oldItemPosition],
            newList[newItemPosition]
        )
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return diffCallback.getChangePayload(
            oldList[oldItemPosition],
            newList[newItemPosition]
        )
    }
}
