/* (C)2020 */
package com.qihuan.wanandroid

import android.app.Application
import com.qihuan.wanandroid.app.LogUtil
import com.tencent.mmkv.MMKV
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this

        UMConfigure.init(
            this,
            BuildConfig.APP_UMENG_KEY,
            "Umeng",
            UMConfigure.DEVICE_TYPE_PHONE,
            ""
        )
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL)

        val cachePath = context.getExternalFilesDir("mmkv")
        if (null != cachePath) {
            MMKV.initialize(cachePath.absolutePath)
        } else {
            val initPath = MMKV.initialize(this)
            LogUtil.d { "MMKV init in path: $initPath" }
        }
    }

    companion object {
        lateinit var context: Application
    }
}
