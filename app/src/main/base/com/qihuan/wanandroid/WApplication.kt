/* (C)2020 */
package com.qihuan.wanandroid

import android.app.Application
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
    }

    companion object {
        lateinit var context: Application
    }
}
