package com.chenpeng.simpleToDo.stroe.net

import com.chenpeng.simpleToDo.entities.BaseResult
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class ContextObserver<T> : Observer<T> {

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

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        onFailure(e)
    }

    abstract fun onSuccess(baseResult: T)
    abstract fun onFailure(e: Throwable)
}