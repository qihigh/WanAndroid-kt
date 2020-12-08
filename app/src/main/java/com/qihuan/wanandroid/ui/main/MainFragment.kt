/* (C)2020 */
package com.qihuan.wanandroid.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.qihuan.wanandroid.MainActivity
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.BaseFragment
import com.qihuan.wanandroid.app.JsonUtil
import com.qihuan.wanandroid.network.RetrofitApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var jsonUtil: JsonUtil

    @Inject
    lateinit var loginService: RetrofitApi.LoginApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//
        // TODO: Use the ViewModel

        view?.findViewById<View>(R.id.message)?.setOnClickListener {
            Snackbar.make(view!!, "test", Snackbar.LENGTH_SHORT).show()

//            val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            viewModel.register()
        }
    }
}
