package com.chenpeng.simpleToDo.stroe.net

import android.accounts.NetworkErrorException
import com.chenpeng.simpleToDo.App
import com.chenpeng.simpleToDo.entities.BaseResult
import com.chenpeng.simpleToDo.utils.LogUtils
import com.chenpeng.simpleToDo.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.SocketTimeoutException

abstract class BaseObserver<T> : Observer<T> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {
        if (t == null) {
            onError(Throwable(NullPointerException()))
            return
        }
        val baseResult = t as BaseResult<*>
        LogUtils.d("code == " + baseResult.code)
        LogUtils.d("message == " + baseResult.message)

        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        when (e) {
            is NetworkErrorException -> {
                ToastUtils.show(App.get(), "网络错误")
            }
            is SocketTimeoutException -> {
                ToastUtils.show(App.get(), "网络错误")
            }
            is HttpException -> {
                ToastUtils.show(App.get(), "网络错误--" + e.code())
            }
            else -> {
                ToastUtils.show(App.get(), "未知错误")
            }
        }
        onFailure(e)
    }

    abstract fun onSuccess(baseResult: T)
    abstract fun onFailure(e: Throwable)
}