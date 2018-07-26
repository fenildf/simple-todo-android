package com.chenpeng.simpleToDo

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.support.multidex.MultiDex
import com.chenpeng.simpleToDo.dao.AppDatabase
import com.chenpeng.simpleToDo.entities.UserBean
import com.chenpeng.simpleToDo.stroe.db.LocalStore
import com.chenpeng.simpleToDo.utils.LogCatStrategy
import com.chenpeng.simpleToDo.utils.LogUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.simple.spiderman.SpiderMan


class App : Application() {

    companion object {
        private lateinit var mApp: App

        fun get(): App {
            return mApp
        }

    }

    var user: UserBean? = null
        get() = when (field == null) {
            true -> {
                LocalStore.getUser()
            }
            else -> {
                field
            }
        }

    var db: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        mApp = this
        initSpiderMan()
        initLogger()
        db = Room.databaseBuilder(this,
                AppDatabase::class.java, "simple_todo")
                .allowMainThreadQueries()
                .build()
        initRefresh()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(3)        // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(LogCatStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(LogUtils.TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    private fun initSpiderMan() {
        SpiderMan.getInstance()
                .init(this)
                //设置是否捕获异常，不弹出崩溃框
                .setEnable(true)
                //设置是否显示崩溃信息展示页面
                .showCrashMessage(true)
                //是否回调异常信息，友盟等第三方崩溃信息收集平台会用到,
                .setOnCrashListener { _, _, _ ->
                    //CrashModel 崩溃信息记录，包含设备信息
                }
    }

    private fun initRefresh() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            ClassicsHeader(context)
            //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }

    var isLogin = false

}