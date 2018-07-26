package com.chenpeng.simpleToDo.stroe.net

import com.chenpeng.simpleToDo.config.BaseConfig
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiFactory {

    companion object {
        private val base_url = BaseConfig.BASE_URL

        private val retrofit = Retrofit.Builder()
                .baseUrl(base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .disableHtmlEscaping()
                        .create()))
                .build()

        private val mApi = retrofit.create(Api::class.java)

        fun api(): Api {
            return mApi
        }
    }


}