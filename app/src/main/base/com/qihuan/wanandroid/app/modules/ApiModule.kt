/* (C)2020 */
package com.qihuan.wanandroid.app.modules

import com.qihuan.wanandroid.network.RetrofitApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * 全局
 */
@InstallIn(ApplicationComponent::class)
@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideLoginService(retrofitAdapter: Retrofit): RetrofitApi.LoginApi {
        return retrofitAdapter.create(RetrofitApi.LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleService(retrofitAdapter: Retrofit): RetrofitApi.ArticleApi {
        return retrofitAdapter.create(RetrofitApi.ArticleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofitAdapter: Retrofit): RetrofitApi.UserApi {
        return retrofitAdapter.create(RetrofitApi.UserApi::class.java)
    }
}
