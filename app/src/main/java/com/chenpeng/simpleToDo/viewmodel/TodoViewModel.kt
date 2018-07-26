package com.chenpeng.simpleToDo.viewmodel

import android.arch.lifecycle.ViewModel
import com.chenpeng.simpleToDo.entities.BaseResult
import com.chenpeng.simpleToDo.entities.ToDoBean
import com.chenpeng.simpleToDo.stroe.net.ApiFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * author : ChenPeng
 * date : 2018/4/13
 * description :
 */
class TodoViewModel : ViewModel() {

    fun add(u_id: Int?, content: String) = when (u_id != null) {
        true -> {
            ApiFactory.api().todo_add(u_id!!, content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        else -> {
            null
        }
    }

    fun list(u_id: Int?, start: Int, count: Int): Observable<BaseResult<List<ToDoBean>>> {
        return ApiFactory.api().todo_list(u_id ?: 1, start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun finished(u_id: Int?, start: Int, count: Int) = when (u_id != null) {
        true -> {
            ApiFactory.api().todo_finished(u_id!!, start, count)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        false -> {
            null
        }
    }

    fun finish(u_id: Int?, todoId: Int) = when (u_id != null) {
        true -> {
            ApiFactory.api().todo_finish(u_id!!, todoId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        false -> {
            null
        }
    }

    fun delete(u_id: Int?, todoId: Int) = when (u_id != null) {
        true -> {
            ApiFactory.api().todo_delete(u_id!!, todoId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        false -> {
            null
        }
    }

    fun update(u_id: Int?, todoId: Int, content: String) = when (u_id != null) {
        true -> {
            ApiFactory.api().todo_update(u_id!!, todoId, content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        false -> {
            null
        }
    }
}