package com.qihuan.wanandroid.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.BaseFragment
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

    override fun initView() {
        val homeList = findViewById<ERecyclerView>(R.id.home_list)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

        toolbar.title = "WanAndroid"
//        toolbar.setCollapseIcon(R.drawable.ic_home)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val homeAdapter = HomeAdapter(requireContext(), emptyList(), viewModel)
        wrapperAdapter(homeAdapter)
        homeList.setLayoutManager(LinearLayoutManager(requireContext()))
        homeList.addItemDecoration(SimpleDividerDecoration(requireContext()))
        homeList.setAdapter(homeAdapter)
        homeList.setRefreshListener(this)
        homeList.setRefreshing(isRefreshing = true, isCallbackListener = true)

        //监听数据变化
        viewModel.getArticleListLv().observe(viewLifecycleOwner, Observer { loadEvent ->
            commonLoad(
                homeAdapter,
                homeList,
                loadEvent,
                itemCallback = DIFF_ARTICLE
            )
        })
    }

    override fun doBusiness() {
    }

    override fun onLoadMore() {
        viewModel.load(false)
    }

    override fun onRefresh() {
        viewModel.load(true)
    }


}