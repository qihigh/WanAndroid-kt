package com.qihuan.wanandroid.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qihuan.wanandroid.app.BaseViewModel
import com.qihuan.wanandroid.app.LogUtil
import com.qihuan.wanandroid.app.applyResponseTransform
import com.qihuan.wanandroid.app.applyUIAsync
import com.qihuan.wanandroid.app.viewmodel.ISnackViewModel
import com.qihuan.wanandroid.app.viewmodel.SnackViewModel
import com.qihuan.wanandroid.model.UserInfo
import com.qihuan.wanandroid.network.RetrofitApi

interface ILoginViewModel : ISnackViewModel {
    fun setUserName(userName: String)
    fun setPsd(psd: String)
    fun login()
    fun getLoginBtnEnable(): LiveData<Boolean>
    fun toggleRegister(register: Boolean)
    fun getRegisterMode(): LiveData<Boolean>
    fun setPsdTwice(psdTwice: String)
    fun register()
    fun getUserInfo(): LiveData<UserInfo>
}

class LoginViewModel @ViewModelInject constructor(
    private val loginService: RetrofitApi.LoginApi,
) : BaseViewModel(), ILoginViewModel, ISnackViewModel by SnackViewModel() {

    private val userInfo = MutableLiveData<UserInfo>()
    private val loginBtnEnable = MutableLiveData(false)
    private val registerEnable = MutableLiveData(false)
    private var userName: String? = null
    private var userPsd: String? = null
    private var userPsdTwice: String? = null

    override fun login() {
        loginService.login(userName!!, userPsd!!)
            .compose(applyResponseTransform())
            .compose(applyUIAsync())
            .subscribe({
                userInfo.postValue(it.data)
            }, {
                _snackMsg.postValue("登录失败 $it")
                LogUtil.e { it }
            }).let {
                accept(it)
            }
    }

    override fun getLoginBtnEnable(): LiveData<Boolean> = loginBtnEnable

    override fun setUserName(userName: String) {
        this.userName = userName
        updateLoginBtn()
    }

    override fun setPsd(psd: String) {
        this.userPsd = psd
        updateLoginBtn()
    }


    override fun setPsdTwice(psdTwice: String) {
        this.userPsdTwice = psdTwice
    }

    override fun register() {
        if (this.userPsd != this.userPsdTwice) {
            _snackMsg.postValue("两次密码输入不同，请重新输入")
            return
        }
        loginService.register(userName!!, userPsd!!, userPsd!!)
            .compose(applyResponseTransform())
            .compose(applyUIAsync())
            .subscribe({
                userInfo.postValue(it.data)
            }, {
                _snackMsg.postValue("注册失败 $it")
                LogUtil.e { it }
            }).let {
                accept(it)
            }
    }

    override fun getUserInfo(): LiveData<UserInfo> = userInfo

    override fun toggleRegister(register: Boolean) {
        _snackMsg.postValue("注册")
        registerEnable.postValue(register)
    }

    override fun getRegisterMode(): LiveData<Boolean> = registerEnable


    private fun updateLoginBtn() {
        if (!this.userName.isNullOrBlank() && !this.userPsd.isNullOrBlank()) {
            loginBtnEnable.postValue(true)
        } else {
            loginBtnEnable.postValue(false)
        }
    }
}