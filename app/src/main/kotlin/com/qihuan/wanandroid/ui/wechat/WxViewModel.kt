/* (C)2020 */
package com.qihuan.wanandroid.ui.wechat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.qihuan.wanandroid.app.BaseViewModel
import com.qihuan.wanandroid.app.JsonUtil
import com.qihuan.wanandroid.app.applyResponseTransform
import com.qihuan.wanandroid.app.applyUIAsync
import com.qihuan.wanandroid.model.WxChannel
import com.qihuan.wanandroid.network.RetrofitApi
import com.tencent.mmkv.MMKV

const val KEY_WX_CHANNEL = "KEY_WX_CHANNEL"

class WxViewModel @ViewModelInject constructor(
    private val wxService: RetrofitApi.WxApi,
    private val cache: MMKV,
    private val jsonUtil: JsonUtil,
) : BaseViewModel(), IWxViewModel {

    private var wxChannelLV = MutableLiveData<List<WxChannel>>().apply {
        value = emptyList()
    }

    override fun getWxChannelLV(): LiveData<List<WxChannel>> = wxChannelLV

    override fun loadChannel() {
        // 优先从缓存中获取
        val cacheJson = cache.decodeString(KEY_WX_CHANNEL)
        cacheJson?.let { theCacheJson ->
            //TODO fix json cache
            jsonUtil.fromJson<List<WxChannel>>(
                theCacheJson,
                List::class.java,
                WxChannel::class.java
            )?.let {
                wxChannelLV.postValue(it)
                return
            }
        }

        wxService.listWxChannel()
            .compose(applyResponseTransform())
            .compose(applyUIAsync())
            .subscribe(
                { apiResponse ->
                    apiResponse.data?.let { channels ->
                        wxChannelLV.postValue(channels)
                        cache.encode(KEY_WX_CHANNEL, jsonUtil.toJson(channels))
                    }
                },
                {
                    ToastUtils.showLong(it.message)
                }
            ).let {
                accept(it)
            }
    }
}

interface IWxViewModel {
    fun loadChannel()
    fun getWxChannelLV(): LiveData<List<WxChannel>>
}
