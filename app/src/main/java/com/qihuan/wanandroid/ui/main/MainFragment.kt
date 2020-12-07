/* (C)2020 */
package com.qihuan.wanandroid.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.BaseFragment
import com.qihuan.wanandroid.app.JsonUtil
import com.qihuan.wanandroid.app.LogUtil
import com.qihuan.wanandroid.model.ApiResponse
import com.qihuan.wanandroid.network.RetrofitApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var jsonUtil: JsonUtil

    @Inject
    lateinit var loginService: RetrofitApi.LoginApi

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        view?.findViewById<View>(R.id.message)?.setOnClickListener {
            Snackbar.make(view!!, "test", Snackbar.LENGTH_SHORT).show()
        }
    }


}
