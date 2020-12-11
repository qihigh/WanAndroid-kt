/* (C)2020 */
package com.qihuan.wanandroid.ui.wechat

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.BaseFragment
import com.qihuan.wanandroid.app.LogUtil
import com.qihuan.wanandroid.eRecycle.ERecyclerView
import com.qihuan.wanandroid.eRecycle.ICommonAdapter
import com.qihuan.wanandroid.model.WxChannel
import com.qihuan.wanandroid.ui.SimpleDividerDecoration
import com.qihuan.wanandroid.ui.home.HomeAdapter
import com.qihuan.wanandroid.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WxArticleFragment : BaseFragment(R.layout.wx_article_fragment), ICommonAdapter {
    companion object {
        private const val INT_WX_ARTICLE_ID: String = "INT_WX_ARTICLE_ID"

        fun newInstance(wxChannel: WxChannel): WxArticleFragment {
            val result = WxArticleFragment()
            result.arguments = Bundle().apply {
                putInt(INT_WX_ARTICLE_ID, wxChannel.id)
            }
            return result
        }
    }

    private lateinit var articleList: ERecyclerView
    private val viewModel: IWxArticleViewModel by viewModels<WxArticleViewModel>()

    override fun initData(bundle: Bundle?) {
        bundle?.getInt(INT_WX_ARTICLE_ID)?.let {
            viewModel.setWxArticleId(it)
        }
    }

    override fun initView() {
        LogUtil.d { viewModel.toString() }
        articleList = findViewById<ERecyclerView>(R.id.wx_article)

        val homeAdapter = HomeAdapter(requireContext(), emptyList(), viewModel)
        wrapperAdapter(homeAdapter)
        articleList.setLayoutManager(LinearLayoutManager(requireContext()))
        articleList.addItemDecoration(SimpleDividerDecoration(requireContext()))
        articleList.setAdapter(homeAdapter)
        articleList.setRefreshListener(this)

        // 监听数据变化
        viewModel.getArticleListLv().observe(
            viewLifecycleOwner,
            Observer { loadEvent ->
                commonLoad(
                    homeAdapter,
                    articleList,
                    loadEvent,
                    itemCallback = HomeViewModel.DIFF_ARTICLE
                )
            }
        )
    }

    override fun doBusiness() {
        LogUtil.d { viewModel.toString() }
        articleList.setRefreshing(isRefreshing = true, isCallbackListener = true)
    }

    override fun onLoadMore() {
        viewModel.load(false)
    }

    override fun onRefresh() {
        viewModel.load(true)
    }
}
