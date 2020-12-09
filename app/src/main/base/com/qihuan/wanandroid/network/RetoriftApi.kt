/* (C)2020 */
package com.qihuan.wanandroid.network

import com.qihuan.wanandroid.model.ApiResponse
import com.qihuan.wanandroid.model.ArticleList
import com.qihuan.wanandroid.model.UserInfo
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

sealed class RetrofitApi {
    interface LoginApi {
        /**
         * 登录
         *
         * @param username 账号
         * @param password 密码
         * @return
         */
        @FormUrlEncoded
        @POST("user/login")
        fun login(
            @Field("username") username: String,
            @Field("password") password: String,
        ): Observable<ApiResponse<UserInfo>>

        @FormUrlEncoded
        @POST("user/register")
        fun register(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("repassword") rePassword: String,
        ): Observable<ApiResponse<UserInfo>>

        @GET("user/logout/json")
        fun logout(): Observable<ApiResponse<String>>
    }

    interface ProjectApi

    interface TreeApi

    interface ArticleApi {
        /**
         * 获取文章列表
         *
         * @param page 页码，拼接在连接中，从0开始。
         * @return
         */
        @GET("article/list/{page}/json")
        fun listArticle(@Path("page") page: Int): Observable<ApiResponse<ArticleList>>
    }

    interface UserApi
}
