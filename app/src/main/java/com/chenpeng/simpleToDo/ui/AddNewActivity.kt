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
import com.chenpeng.simpleToDo.utils.EventBusUtils
import com.chenpeng.simpleToDo.utils.MessageEvent
import com.chenpeng.simpleToDo.viewmodel.Live
import com.chenpeng.simpleToDo.viewmodel.TodoViewModel

class AddNewActivity : DataBindingActivity<ActivityAddNewBinding>(), View.OnClickListener {

    private lateinit var viewModel: TodoViewModel
    private var mTodoBean: ToDoBean? = null
    private var mPosition: Int? = null

    companion object {
        const val ToDoBean = "ToDo"
        const val POSITION = "position"
    }

    override fun layoutId(): Int {
        return R.layout.activity_add_new
    }

    override fun init(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        mTodoBean = intent.getSerializableExtra(ToDoBean) as ToDoBean?
        mPosition = intent.getIntExtra(POSITION, 0)
        initView()
    }


    private fun initView() {
        var toolbarTitle: String
        if (mTodoBean == null) {
            toolbarTitle = getString(R.string.add_new)
        } else {
            toolbarTitle = getString(R.string.update)
            vdb.etTitile.setText(mTodoBean?.content)
        }
        vdb.toolbarAddNew.title = toolbarTitle
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
                    if (mTodoBean == null) {
                        addTodo(content)
                    } else {
                        if (App.get().user == null || App.get().user?.id == null) {
                            return
                        }
                        if (mTodoBean == null || mTodoBean?.id == null) {
                            return
                        }
                        updateTodo(App.get().user?.u_id!!, mTodoBean?.id!!, content)
                    }
                }

            }
        }
    }

    private fun addTodo(content: String) {

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
                            intent.putExtra("todo", it)
                            setResult(Activity.RESULT_OK, intent)
                        }
                        toast(baseResult.message)
                        finish()
                    }

                    override fun onFailure(e: Throwable) {

                    }
                })

    }

    private fun updateTodo(u_id: Int, id: Int, content: String) {
        viewModel.update(u_id, id, content)
                ?.subscribe(object : BaseObserver<BaseResult<ToDoBean>>() {
                    override fun onSuccess(baseResult: BaseResult<ToDoBean>) {
                        if (baseResult.isFailure()) {
                            toast(baseResult.message)
                            return
                        }
                        toast(baseResult.message)
                        val messageEvent = MessageEvent()
                        messageEvent.what = MessageEvent.REFRESH_TODO
                        EventBusUtils.post(messageEvent)
                        finish()
                    }

                    override fun onFailure(e: Throwable) {
                    }

                })
    }
}