/* (C)2020 */
package com.qihuan.wanandroid.ui.wechat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qihuan.wanandroid.app.BaseListViewModel
import com.qihuan.wanandroid.app.applyResponseTransform
import com.qihuan.wanandroid.app.applyUIAsync
import com.qihuan.wanandroid.eRecycle.LoadEvent
import com.qihuan.wanandroid.eRecycle.LoadEventType
import com.qihuan.wanandroid.model.Article
import com.qihuan.wanandroid.network.RetrofitApi
import com.qihuan.wanandroid.ui.home.IHomeViewModel
import io.reactivex.Observable

class WxArticleViewModel @ViewModelInject constructor(
    private val wxService: RetrofitApi.WxApi,
) : BaseListViewModel<Article>(), IWxArticleViewModel {

    private var wxArticleId: Int = 0
    override var pageStart = 1

    override fun setWxArticleId(articleId: Int) {
        wxArticleId = articleId
    }

    private var articleListLV = MutableLiveData<LoadEvent<Article>>().apply {
        value = LoadEvent(LoadEventType.load_new_empty, emptyList())
    }

    override fun getArticleListLv(): LiveData<LoadEvent<Article>> = articleListLV

    override fun showData(loadEvent: LoadEvent<Article>) {
        articleListLV.postValue(loadEvent)
    }

    override fun doLoadData(loadNew: Boolean, skipCache: Boolean): Observable<List<Article>> {
        return wxService.loadWxArticle(wxArticleId, page)
            .compose(applyResponseTransform())
            .compose(applyUIAsync())
            .map { it.data?.dataList ?: emptyList() }
    }
}

interface IWxArticleViewModel : IHomeViewModel {
    fun setWxArticleId(articleId: Int)
}
