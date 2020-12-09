/* (C)2020 */
package com.qihuan.wanandroid

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.blankj.utilcode.util.LogUtils
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED; // 竖屏

//        initImmersionBar();
        ImmersionBar.with(this)
            .fitsSystemWindows(true) // 使用该属性,必须指定状态栏颜色
            .statusBarColor(R.color.w_disable)
            .init()

//        val navController = findNavController(R.id.container)
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            LogUtil.d { "nav to ${destination.label}" }
//        }
//
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, LoginFragment.newInstance())
//                .commitNow()
//        }
    }

    private fun initImmersionBar() {
        val b = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        if (!fullScreen()) {
            if (!transparent()) {
                if (ImmersionBar.isSupportStatusBarDarkFont()) {
                    ImmersionBar.with(this)
                        .keyboardEnable(true)
                        .statusBarColor(R.color.w_disable)
                        .statusBarDarkFont(!b)
                        .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                        .init()
                } else {
                    LogUtils.i("当前设备不支持状态栏字体变色")
                    ImmersionBar.with(this)
                        .keyboardEnable(true)
                        .statusBarColor(R.color.w_disable)
                        .statusBarDarkFont(!b)
                        .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                        .navigationBarDarkIcon(true)
                        .init()
                }
            } else {
                ImmersionBar.with(this)
                    .statusBarView(R.id.statusBarView)
                    .statusBarDarkFont(!b)
                    .keyboardEnable(true)
                    .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                    .init()
            }
        } else {
            ImmersionBar.with(this)
                .fullScreen(true)
                .keyboardEnable(true)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init()
        }
    }

    private fun transparent(): Boolean {
        return true
    }

    private fun fullScreen(): Boolean {
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.container)
        return super.onSupportNavigateUp()
    }
}
