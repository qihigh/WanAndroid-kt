/* (C)2020 */
package com.qihuan.wanandroid.app.modules

import com.qihuan.wanandroid.app.JsonUtil
import com.qihuan.wanandroid.network.RetrofitApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {

    /**
     * json相关
     */
    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Singleton
    @Provides
    fun provideJsonUtil(moshi: Moshi): JsonUtil {
        return JsonUtil(moshi)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            // 失败重连
            .retryOnConnectionFailure(true)
//            .addInterceptor(HttpLoggingInterceptor("httpLog")) //添加打印拦截器
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitAdapter(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        val baseUrl = "https://www.wanandroid.com/"
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideLoginService(retrofitAdapter: Retrofit): RetrofitApi.LoginApi {
        return retrofitAdapter.create(RetrofitApi.LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(retrofitAdapter: Retrofit): RetrofitApi.UserApi {
        return retrofitAdapter.create(RetrofitApi.UserApi::class.java)
    }

    /**
     * 账户相关
     */
    @Singleton
    @Provides
    fun provideAccount(): String {
        throw Exception()
    }
}
