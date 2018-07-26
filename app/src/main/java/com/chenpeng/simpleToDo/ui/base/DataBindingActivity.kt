package com.chenpeng.simpleToDo.ui.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class DataBindingActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var vdb: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vdb = DataBindingUtil.setContentView(this, layoutId())
        init(savedInstanceState)
    }

    abstract fun layoutId(): Int
    abstract fun init(savedInstanceState: Bundle?)
}