/* (C)2020 */
package com.qihuan.wanandroid.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.BaseFragment
import com.qihuan.wanandroid.app.JsonUtil
import com.qihuan.wanandroid.network.RetrofitApi
import com.qihuan.wanandroid.ui.home.HomeFragment
import com.qihuan.wanandroid.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var jsonUtil: JsonUtil

    @Inject
    lateinit var loginService: RetrofitApi.LoginApi

    override fun initData(bundle: Bundle?) {
    }

    override fun initView() {
        view?.findViewById<View>(R.id.message)?.setOnClickListener {
            Snackbar.make(requireView(), "test", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_main_to_login)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main_bottom)
        val viewPager = findViewById<ViewPager2>(R.id.main_viewpager)
        //选中
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.tab_home -> {
                    viewPager.setCurrentItem(0, false)
                }
                R.id.tab_question -> {
                    viewPager.setCurrentItem(1, false)
                }
                R.id.tab_me -> {
                    viewPager.setCurrentItem(2, false)
                }
                R.id.tab_tree -> {
                    viewPager.setCurrentItem(3, false)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        /**
         * 再次点击刷新
         */
        bottomNavigationView.setOnNavigationItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.tab_home -> {

                }
                R.id.tab_question -> {

                }
                R.id.tab_me -> {

                }
                R.id.tab_tree -> {

                }
            }
        }

        //默认选中第一个 不做处理。

        //处理viewPager
        viewPager.offscreenPageLimit = 3
        viewPager.isUserInputEnabled = false//禁止滑动
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 4

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> HomeFragment.newInstance()
                    else -> {
                        ProfileFragment.newInstance()
                    }
                }
            }
        }

    }


    override fun doBusiness() {
    }
}
