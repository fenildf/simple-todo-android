package com.chenpeng.simpleToDo.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.chenpeng.simpleToDo.App
import com.chenpeng.simpleToDo.R
import com.chenpeng.simpleToDo.databinding.ActivityAddNewBinding
import com.chenpeng.simpleToDo.entities.BaseResult
import com.chenpeng.simpleToDo.entities.ToDoBean
import com.chenpeng.simpleToDo.showError
import com.chenpeng.simpleToDo.stroe.net.BaseObserver
import com.chenpeng.simpleToDo.toast
import com.chenpeng.simpleToDo.ui.base.DataBindingActivity
import com.chenpeng.simpleToDo.viewmodel.Live
import com.chenpeng.simpleToDo.viewmodel.TodoViewModel

class AddNewActivity : DataBindingActivity<ActivityAddNewBinding>(), View.OnClickListener {

    private lateinit var viewModel: TodoViewModel

    override fun layoutId(): Int {
        return R.layout.activity_add_new
    }

    override fun init(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)

        initView()
    }


    private fun initView() {
        vdb.toolbarAddNew.setNavigationOnClickListener {
            finish()
        }

        vdb.btnSure.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.btn_sure -> {
                    val content = vdb.etTitile.text.toString()
                    if (TextUtils.isEmpty(content)) {
                        showError("内容不能为空")
                        return
                    }
                    addTodo(content)
                }

            }
        }
    }

    fun addTodo(content: String) {

        viewModel.add(App.get().user?.u_id, content)
                ?.compose(Live.bindLifecycle(this))
                ?.subscribe(object : BaseObserver<BaseResult<ToDoBean>>() {

                    override fun onSuccess(baseResult: BaseResult<ToDoBean>) {
                        if (baseResult.code != BaseResult.SUCCESS) {
                            showError(baseResult.message)
                            return
                        }
                        baseResult.result?.let {
                            val intent = Intent()
                            intent.putExtra("todo",it)
                            setResult(Activity.RESULT_OK,intent)
                        }
                        toast(baseResult.message)
                        finish()
                    }

                    override fun onFailure(e: Throwable) {

                    }
                })

    }
}