import com.qihuan.versionplugin.Ext.Versions
import com.qihuan.versionplugin.Ext.Libs

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.compileSdkVersion)

    defaultConfig {
        applicationId("com.qihuan.wanandroid")
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        versionCode(1)
        versionName("1.0.0")

        buildConfigField("String", "APP_UMENG_KEY", "\"5f4737cc97106e71f6e1bd2c\"")

        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
        }

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    sourceSets {
        findByName("main")?.apply {
            java.srcDirs("src/main/kotlin")
            java.srcDirs("src/main/base")
        }
    }

    signingConfigs {
        val sign: String by project
        create("myConfig") {
            storeFile = file("mysign.jks")
            storePassword = sign
            keyAlias = sign
            keyPassword = sign
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("myConfig")
            minifyEnabled(false)
            proguardFiles("proguard-rules.pro")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("myConfig")
            minifyEnabled(false)
            proguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    lintOptions {
        isAbortOnError = false
        isCheckReleaseBuilds = false
        disable(
            "MissingTranslation",
            "StringFormatCount",
            "IconDipSize",
            "SmallSp",
            "ContentDescription",
            "SetTextI18n",
            "HardcodedText",
            "RtlHardcoded",
            "Typos"
        )
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.kotlin)
    implementation(Libs.AndroidX.Core)
    implementation(Libs.AndroidX.Appcompat)
    implementation(Libs.AndroidX.Material)
    implementation(Libs.AndroidX.ConstraintLayout)
    implementation(Libs.AndroidX.LifecycleLivedata)
    implementation(Libs.AndroidX.LifecycleViewModel)
    implementation(Libs.AndroidX.SwipeRefreshLayout)

    implementation(Libs.RxJava)
    implementation(Libs.RxAndroid)
//    implementation(Libs.RxPermissions)
    implementation(Libs.Glide)
    implementation(Libs.Mmkv)
    implementation(Libs.ButterKnife)
    kapt(Libs.ButterKnifeCompiler)
    implementation(Libs.UmCommon)
    implementation(Libs.UmAsms)
    implementation(Libs.UmCrash)
    implementation(Libs.Dagger)
    kapt(Libs.DaggerCompiler)
    implementation(Libs.Moshi)
    kapt(Libs.MoshiCodegen)
    implementation(Libs.Retrofit)
    implementation(Libs.MoshiConverter)
    implementation(Libs.RxJavaAdapter)


    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}