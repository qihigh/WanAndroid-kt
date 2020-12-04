plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

/**
 * 从properties文件中读取数据
 *
 * kts 从gradle5.6以后的版本开始不再支持buildSrc，这里退而采用properties的方式来定义共用的值
 */
fun ext(name: String): String = project.property(name) as String


android {
    compileSdkVersion(ext("compileSdkVersion"))

    defaultConfig {
        applicationId("com.qihuan.wanandroid")
        minSdkVersion(ext("minSdkVersion"))
        targetSdkVersion(ext("targetSdkVersion"))
        versionCode(1)
        versionName("1.0.0")

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
        val sign = ext("sign")
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

/**
 * 拓展 dependencies
 */
fun DependencyHandler.implementationExt(name: String): Dependency? =
    add("implementation", ext(name))

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${ext("kotlinVersion")}")
    implementationExt("androidXCore")
    implementationExt("androidXAppcompat")
    implementationExt("androidXMaterial")
    implementationExt("androidXConstraintLayout")
    implementationExt("androidXLifecycleLivedata")
    implementationExt("androidXLifecycleViewModel")

    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}