plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

fun ext(name: String): String {
    return project.property(name) as String
}

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
        create("myConfig") {
            storeFile = file("mysign.jks")
            storePassword = "qihuan"
            keyAlias = "qihuan"
            keyPassword = "qihuan"
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

    kotlinOptions {
        jvmTarget = "1.8"
    }
}



dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${ext("kotlinVersion")}")
    implementation(ext("androidXCore"))
    implementation(ext("androidXAppcompat"))
    implementation(ext("androidXMaterial"))
    implementation(ext("androidXConstraintLayout"))
    implementation(ext("androidXLifecycleLivedata"))
    implementation(ext("androidXLifecycleViewModel"))


    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}