// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    // kts 从gradle5.6以后的版本开始不再支持buildSrc，这里退而采用properties的方式来定义共用的值
    val kotlinVersion: String by project
    val kotlinGradlePlugin: String by project

    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/thelasterstar/maven/")
        // 添加阿里云 maven 地址
        maven("http://maven.aliyun.com/nexus/content/repositories/jcenter")
        //对应google
        maven("https://maven.aliyun.com/repository/google")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$kotlinGradlePlugin")
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.diffplug.spotless").version("5.7.0")
    //启用自定义插件，引入通用版本信息 只有subprojects可以引用
    id("com.qihuan.versionplugin").apply(false)
}

subprojects {
    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/thelasterstar/maven/")
        // 添加阿里云 maven 地址
        maven("http://maven.aliyun.com/nexus/content/repositories/jcenter")
        //对应google
        maven("https://maven.aliyun.com/repository/google")
        maven("https://dl.bintray.com/umsdk/release")
    }

    /**
     * 1.添加kotlin校验和格式化
     * 2.引入版本信息
     */
    apply {
        plugin("com.diffplug.spotless")
        plugin("com.qihuan.versionplugin")
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        format("misc") {
            target("*.kt", "*.md")

            // define the steps to apply to those files
            trimTrailingWhitespace()
            indentWithTabs() // or spaces. Takes an integer argument if you don't like 4
            endWithNewline()
        }

        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint(com.qihuan.versionplugin.Ext.Versions.ktLintVersion)
            // 添加header
            licenseHeader("/* (C)2020 */")
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = false

            jvmTarget = "1.8"//JavaVersion.VERSION_1_8.name
            // 优化kotlin编译后的自动校验代码. [https://juejin.im/post/5e1c6163f265da3e4736b37f]
            // 通过反编译进行确认,的确能够去掉对应的代码.适合在release版本启用.
            freeCompilerArgs = listOf<String>(
                "-Xno-call-assertions",
                "-Xno-receiver-assertions",
                "-Xno-param-assertions"
            )
        }
    }
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}