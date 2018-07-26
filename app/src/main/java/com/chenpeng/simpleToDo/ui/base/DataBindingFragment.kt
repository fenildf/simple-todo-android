package com.chenpeng.simpleToDo.ui.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class DataBindingFragment<T : ViewDataBinding> : Fragment() {

    protected lateinit var vdb: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        vdb = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        return vdb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view, savedInstanceState)
    }

    abstract fun layoutId(): Int
    abstract fun init(view: View?, savedInstanceState: Bundle?)
}