/* (C)2020 */
package com.qihuan.wanandroid.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.BaseFragment
import com.qihuan.wanandroid.app.JsonUtil
import com.qihuan.wanandroid.network.RetrofitApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

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
//            val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            viewModel.register()
        }
    }

    override fun doBusiness() {
    }
}
