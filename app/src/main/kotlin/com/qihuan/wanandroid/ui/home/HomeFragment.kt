/* (C)2020 */
package com.qihuan.wanandroid.ui.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.BaseFragment
import com.qihuan.wanandroid.app.LogUtil
import com.qihuan.wanandroid.eRecycle.ERecyclerView
import com.qihuan.wanandroid.eRecycle.ICommonAdapter
import com.qihuan.wanandroid.ui.SimpleDividerDecoration
import com.qihuan.wanandroid.ui.home.HomeViewModel.Companion.DIFF_ARTICLE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.home_fragment), ICommonAdapter {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: IHomeViewModel by viewModels<HomeViewModel>()

    override fun initData(bundle: Bundle?) {
    }

    private lateinit var homeList: ERecyclerView

    override fun initView() {
        LogUtil.d { viewModel.toString() }
        homeList = findViewById<ERecyclerView>(R.id.home_list)

        val homeAdapter = HomeAdapter(requireContext(), emptyList(), viewModel)
        wrapperAdapter(homeAdapter)
        homeList.setLayoutManager(LinearLayoutManager(requireContext()))
        homeList.addItemDecoration(SimpleDividerDecoration(requireContext()))
        homeList.setAdapter(homeAdapter)
        homeList.setRefreshListener(this)

        // 监听数据变化
        viewModel.getArticleListLv().observe(
            viewLifecycleOwner,
            Observer { loadEvent ->
                commonLoad(
                    homeAdapter,
                    homeList,
                    loadEvent,
                    itemCallback = DIFF_ARTICLE
                )
            }
        )
    }

    override fun doBusiness() {
        LogUtil.d { viewModel.toString() }
        homeList.setRefreshing(isRefreshing = true, isCallbackListener = true)
    }

    override fun onLoadMore() {
        viewModel.load(false)
    }

    override fun onRefresh() {
        viewModel.load(true)
    }
}
