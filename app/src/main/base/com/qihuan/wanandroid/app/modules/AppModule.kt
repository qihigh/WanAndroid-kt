/* (C)2020 */
package com.qihuan.wanandroid.app.modules

import com.qihuan.wanandroid.BuildConfig
import com.qihuan.wanandroid.app.JsonUtil
import com.squareup.moshi.Moshi
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 全局提供实例。
 * @provide 是用来提供注入实例的
 * @binds 是用来注入接口实例的。这里暂时未用到
 */
@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    /**
     * json相关
     */
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideJsonUtil(moshi: Moshi): JsonUtil {
        return JsonUtil(moshi)
    }

    @Provides
    @Singleton
    fun provideCache(): MMKV {
        return MMKV.defaultMMKV()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            // 失败重连
            .retryOnConnectionFailure(true)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitAdapter(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        val baseUrl = "https://www.wanandroid.com/"
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * 账户相关
     */
    @Provides
    fun provideAccount(): String {
        throw Exception()
    }
}
