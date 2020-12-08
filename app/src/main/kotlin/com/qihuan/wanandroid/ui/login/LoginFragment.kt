package com.qihuan.wanandroid.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.login_fragment) {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: ILoginViewModel by viewModels<LoginViewModel>()

    override fun initData(bundle: Bundle?) {
    }

    override fun initView() {
        val loginBtn = findViewById<Button>(R.id.login_btn)
        val loginUser = findViewById<EditText>(R.id.login_user)
        val loginPsd = findViewById<EditText>(R.id.login_psd)
        val loginPsdLayoutTwice = findViewById<View>(R.id.login_psd_layout_twice)
        val loginPsdTwice = findViewById<EditText>(R.id.login_psd_twice)
        val loginTips = findViewById<View>(R.id.login_tips)

        loginUser.addTextChangedListener {
            viewModel.setUserName(it.toString())
        }
        loginPsd.addTextChangedListener {
            viewModel.setPsd(it.toString())
        }

        loginPsdTwice.addTextChangedListener {
            viewModel.setPsdTwice(it.toString())
        }

        loginBtn.setOnClickListener {
            if (viewModel.getRegisterMode().value == true) {
                viewModel.register()
            } else {
                viewModel.login()
            }
        }

        loginTips.setOnClickListener {
            viewModel.toggleRegister(true)
        }

        viewModel.getLoginBtnEnable().observe(viewLifecycleOwner, Observer { enable ->
            loginBtn.isEnabled = enable
        })

        viewModel.getRegisterMode().observe(viewLifecycleOwner, Observer { isRegister ->
            loginPsdLayoutTwice.visibility = if (isRegister) View.VISIBLE else View.GONE
            loginBtn.text = if (isRegister) "注册" else "登录"
        })
    }

    override fun doBusiness() {
        viewModel.getUserInfo().observe(viewLifecycleOwner, Observer { userInfo ->
            //登录成功
            viewModel._snackMsg.postValue("登录成功")
        })

        viewModel.snackMsg.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty())
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        })
    }
}