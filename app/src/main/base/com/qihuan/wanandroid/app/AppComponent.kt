/* (C)2020 */
package com.qihuan.wanandroid.app

import com.qihuan.wanandroid.app.modules.AndroidModule
import com.qihuan.wanandroid.app.modules.ApiModule
import com.qihuan.wanandroid.app.modules.AppModule
import com.qihuan.wanandroid.ui.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        AppModule::class,
        ApiModule::class
    ]
)
interface AppComponent {
    fun inject(fragment: MainFragment)
}
