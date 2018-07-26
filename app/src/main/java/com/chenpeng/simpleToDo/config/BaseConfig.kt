package com.chenpeng.simpleToDo.config

import com.chenpeng.simpleToDo.BuildConfig

class BaseConfig {
    companion object {
        val BASE_URL = when (BuildConfig.DEBUG) {
            true -> {
                "http://172.19.1.159:8081"
            }
            else -> {
                "http://118.24.92.99:8081"
            }
        }

        const val PHONE = "phone"
        const val PWD = "pwd"
    }
}