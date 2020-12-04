// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.qihuan.buildsrc.Libs


buildscript {
    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/thelasterstar/maven/")
        // 添加阿里云 maven 地址
        maven("http://maven.aliyun.com/nexus/content/repositories/jcenter")
        //对应google
        maven("https://maven.aliyun.com/repository/google")
    }
    dependencies {
        classpath(Libs.androidGradlePlugin)
        classpath(Libs.Kotlin.gradlePlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/thelasterstar/maven/")
        // 添加阿里云 maven 地址
        maven("http://maven.aliyun.com/nexus/content/repositories/jcenter")
        //对应google
        maven("https://maven.aliyun.com/repository/google")
    }
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

//task clean (type: Delete) {
//    delete rootProject . buildDir
//}