/* (C)2020 */
package com.qihuan.wanandroid.ui.project

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
import dagger.hilt.android.AndroidEntryPoint

/**
 * 项目分类
 */
@AndroidEntryPoint
class ProjectFragment : BaseFragment(R.layout.project_fragment), ICommonAdapter {
    companion object {
        fun newInstance() = ProjectFragment()
    }

    private val viewModel: IProjectViewModel by viewModels<ProjectViewModel>()

    override fun initData(bundle: Bundle?) {
    }
    private lateinit var projectList:ERecyclerView

    override fun initView() {
        LogUtil.d { viewModel.toString() }
        projectList = findViewById<ERecyclerView>(R.id.project_list)

        val projectAdapter = ProjectAdapter(requireContext(), emptyList(), viewModel)
        wrapperAdapter(projectAdapter)
        projectList.setLayoutManager(LinearLayoutManager(requireContext()))
        projectList.addItemDecoration(SimpleDividerDecoration(requireContext()))
        projectList.setAdapter(projectAdapter)
        projectList.setRefreshListener(this)

        // 监听数据变化
        viewModel.getProjectListLv().observe(
            viewLifecycleOwner,
            Observer { loadEvent ->
                commonLoad(
                    projectAdapter,
                    projectList,
                    loadEvent,
                    itemCallback = ProjectViewModel.DIFF_PROJECT
                )
            }
        )
    }

    override fun doBusiness() {
        LogUtil.d { viewModel.toString() }
        projectList.setRefreshing(isRefreshing = true, isCallbackListener = true)
    }

    override fun onLoadMore() {
        viewModel.load(false)
    }

    override fun onRefresh() {
        viewModel.load(true)
    }
}
