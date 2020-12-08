package com.qihuan.versionplugin

object Ext {
    object Versions {
        const val kotlin = "1.4.20"
        const val ktLintVersion = "0.39.0"
        const val compileSdkVersion = "android-29"
        const val targetSdkVersion = "29"
        const val minSdkVersion = "21"
        const val retrofit = "2.6.1"
        const val moshi = "1.11.0"
        const val dagger = "2.16"
        const val hilt = "2.28-alpha"
        const val hiltViewModel = "1.0.0-alpha02"
    }

    object Libs {
        object AndroidX {
            const val Core = "androidx.core:core-ktx:1.3.2"
            const val Appcompat = "androidx.appcompat:appcompat:1.2.0"
            const val Material = "com.google.android.material:material:1.2.1"
            const val Fragment = "androidx.fragment:fragment-ktx:1.2.5"
            const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
            const val LifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
            const val LifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
            const val SwipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
            const val HiltViewModel =
                "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltViewModel}"
            const val HiltViewModelCompiler =
                "androidx.hilt:hilt-compiler:${Versions.hiltViewModel}"
        }


        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"

        const val RxJava = "io.reactivex.rxjava2:rxjava:2.2.11"
        const val RxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
        const val RxPermissions = "com.github.tbruyelle:rxpermissions:0.10.2"
        const val Glide = "com.github.bumptech.glide:glide:4.9.0"
        const val Mmkv = "com.tencent:mmkv-static:1.2.2"
        const val ButterKnife = "com.jakewharton:butterknife:10.0.0"
        const val ButterKnifeCompiler = "com.jakewharton:butterknife-compiler:10.0.0"
        const val UmCommon = "com.umeng.umsdk:common:9.1.0"
        const val UmAsms = "com.umeng.umsdk:asms:1.1.3"
        const val UmCrash = "com.umeng.umsdk:crash:0.0.4"
        const val Dagger = "com.google.dagger:dagger:${Versions.dagger}"
        const val DaggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
        const val Hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val HiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"


        const val Moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
        const val MoshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
        const val Retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val MoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
        const val RxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
        const val OkHttpLogging = "com.squareup.okhttp3:logging-interceptor:4.9.0"
        const val AndroidUtil = "com.blankj:utilcodex:1.30.5"
    }
}