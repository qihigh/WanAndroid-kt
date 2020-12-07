/* (C)2020 */
package com.qihuan.wanandroid.app.modules

import android.app.Application
import com.qihuan.wanandroid.WApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val application: WApplication) {
    @Singleton
    @Provides
    fun provideApplication(): Application = application
}
