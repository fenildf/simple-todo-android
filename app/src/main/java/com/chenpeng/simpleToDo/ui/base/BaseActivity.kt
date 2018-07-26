package com.chenpeng.simpleToDo.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutId())
        init(savedInstanceState)
    }

    abstract fun layoutId(): Int
    abstract fun init(savedInstanceState: Bundle?)
}