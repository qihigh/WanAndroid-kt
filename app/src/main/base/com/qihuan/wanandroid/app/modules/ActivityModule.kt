/* (C)2020 */
package com.qihuan.wanandroid.app.modules

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent

/**
 * activity级别的module，
 * 如果声明了 	@ActivityScoped 	@FragmentScoped，那么目标生命周期中每次注入都是同一个对象
 * 如果没有声明scope，那么每次都是新的对象
 *
 * @link https://developer.android.com/training/dependency-injection/hilt-android#kotlin
 *
 */
@InstallIn(ActivityComponent::class)
@Module
class ActivityModule() {
}
