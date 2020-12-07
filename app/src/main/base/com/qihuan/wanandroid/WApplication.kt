/* (C)2020 */
package com.qihuan.wanandroid

import android.app.Application
import com.qihuan.wanandroid.app.AppComponent
import com.qihuan.wanandroid.app.DaggerAppComponent
import com.qihuan.wanandroid.app.modules.AndroidModule
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

class WApplication : Application() {
    private var appComponent: AppComponent? = null

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

    fun getAppComponent(): AppComponent {
        if (null == appComponent) {
            initAppComponent()
        }
        return appComponent!!
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .androidModule(AndroidModule(this))
            .build()
    }

    companion object {
        lateinit var context: Application
    }
}
