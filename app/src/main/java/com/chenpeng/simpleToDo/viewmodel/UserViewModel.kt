package com.chenpeng.simpleToDo.viewmodel

import android.arch.lifecycle.ViewModel
import com.chenpeng.simpleToDo.entities.BaseResult
import com.chenpeng.simpleToDo.entities.UserBean
import com.chenpeng.simpleToDo.stroe.net.ApiFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserViewModel : ViewModel() {

    fun register(phone: String, pwd: String): Observable<BaseResult<Void>> {
        return ApiFactory.api().user_register(phone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun login(phone: String, pwd: String): Observable<BaseResult<UserBean>> {
        return ApiFactory.api().user_login(phone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}