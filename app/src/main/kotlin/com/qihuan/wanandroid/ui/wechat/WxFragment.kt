/* (C)2020 */
package com.qihuan.wanandroid.ui.wechat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WxFragment : BaseFragment(R.layout.wx_fragment) {
    companion object {
        fun newInstance() = WxFragment()
    }

    private val viewModel: IWxViewModel by viewModels<WxViewModel>()

    override fun initData(bundle: Bundle?) {
    }

    override fun initView() {
        val tabLayout = findViewById<TabLayout>(R.id.wx_tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.wx_viewpager)

        viewModel.getWxChannelLV().observe(
            viewLifecycleOwner,
            Observer { wxChannels ->
                if (wxChannels.isEmpty()) {
                    return@Observer
                }
                if (wxChannels.size <= 4) {
                    tabLayout.tabMode = TabLayout.MODE_FIXED
                } else {
                    tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
                }
                // 添加viewpager2 TODO 解决滑动冲突问题
                viewPager.isUserInputEnabled = true
                // 这里调整缓存大小，不让fragment被回收。
                viewPager.offscreenPageLimit = 1
                (viewPager.getChildAt(0) as RecyclerView).setItemViewCacheSize(wxChannels.size)
                viewPager.adapter = object : FragmentStateAdapter(this) {
                    override fun getItemCount(): Int = wxChannels.size

                    override fun createFragment(position: Int): Fragment {
                        return WxArticleFragment.newInstance(wxChannels[position])
                    }
                }

                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = wxChannels[position].name
                    viewPager.setCurrentItem(tab.position, true)
                }.attach()
            }
        )
    }

    override fun doBusiness() {
        viewModel.loadChannel()
    }
}
