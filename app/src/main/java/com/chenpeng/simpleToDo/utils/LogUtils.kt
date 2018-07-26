package com.chenpeng.simpleToDo.utils

import com.orhanobut.logger.Logger

/**
 * author : ChenPeng
 * date : 2018/4/13
 * description :
 */
class LogUtils {
    companion object {

        const val TAG = "SimpleToDo"

        fun d(msg: String) {
            Logger.d(msg)
        }

        fun e(msg: String) {
            Logger.d(msg)
        }
    }
}