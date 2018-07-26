package com.chenpeng.simpleToDo.entities

class BaseResult<T> {

    var code = -1
    var message = ""
    var result: T? = null

    companion object {
        const val SUCCESS = 0
        const val FAILURE = 1
    }

    fun isSuccess() = code == SUCCESS
    fun isFailure() = code != SUCCESS
}