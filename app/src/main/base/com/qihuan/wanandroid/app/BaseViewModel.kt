package com.qihuan.wanandroid.app

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

open class BaseViewModel : ViewModel(), Consumer<Disposable> {
    private val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }

    override fun accept(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    open fun toggleRegister(register: Boolean) {}
}