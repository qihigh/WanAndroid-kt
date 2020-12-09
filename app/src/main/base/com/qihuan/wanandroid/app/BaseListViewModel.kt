package com.qihuan.wanandroid.app

import com.qihuan.wanandroid.eRecycle.LoadEvent
import com.qihuan.wanandroid.eRecycle.LoadEventType
import io.reactivex.Observable

abstract class BaseListViewModel<T> : BaseViewModel(), IListViewModel<T> {
    /**
     * 控制page的数量，如果是loadNew，则默认为0，每次成功的请求后递增
     */
    protected var page = 0
    protected var lastLoading = 0L

    /**
     * 缓存当前数据
     */
    var data: List<T> = emptyList()

    /**
     * 请求数据库数据
     *
     * @param   isLoadNew 是否是加载新数据
     * @param   skipCache 是否跳过缓存，默认是true
     */
    override fun load(isLoadNew: Boolean, skipCache: Boolean) {
        if (System.currentTimeMillis() - lastLoading < 250) {
            //1秒内不能频繁发送请求
            LogUtil.d { "触发load cancel" }
            showData(LoadEvent(if (isLoadNew) LoadEventType.load_new_cancel else LoadEventType.load_more_cancel))
            return
        }

        lastLoading = System.currentTimeMillis()
        page = if (isLoadNew) 0 else page

        doLoadData(isLoadNew, skipCache)
            .subscribe({ newData ->
                showData(
                    if (isLoadNew) {
                        LogUtil.d { "load new ok $data" }
                        data = newData
                        LoadEvent(LoadEventType.load_new_ok, data.toList())
                    } else {
                        LogUtil.d { "load more ok $newData" }
                        data = data + newData
                        LoadEvent(LoadEventType.load_more_ok, data.toList())
                    }
                )
                //加载完成并成功，允许后续请求
                lastLoading = 0
                page++
            }, {
                showData(
                    if (isLoadNew) {
                        LogUtil.d { "load new err $it" }
                        LoadEvent(LoadEventType.load_new_error, errorMsg = it.message)
                    } else {
                        LogUtil.d { "load more err $it" }
                        LoadEvent(LoadEventType.load_more_error, errorMsg = it.message)
                    }
                )
            }).let {
                accept(it)
            }
    }


    /**
     * 通知view展示数据
     */
    abstract fun showData(loadEvent: LoadEvent<T>)


    /**
     * 子类重写，用于统一加载数据
     */
    abstract fun doLoadData(loadNew: Boolean, skipCache: Boolean): Observable<List<T>>
}

interface IListViewModel<T> {
    /**
     * 请求数据库数据
     *
     * @param   isLoadNew 是否是加载新数据
     * @param   skipCache 是否跳过缓存，默认是true
     */
    fun load(isLoadNew: Boolean, skipCache: Boolean = true)
}
