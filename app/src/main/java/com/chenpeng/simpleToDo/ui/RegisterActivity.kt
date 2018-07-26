package com.chenpeng.simpleToDo.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.chenpeng.simpleToDo.R
import com.chenpeng.simpleToDo.databinding.ActivityRegisterBinding
import com.chenpeng.simpleToDo.entities.BaseResult
import com.chenpeng.simpleToDo.showError
import com.chenpeng.simpleToDo.showSuccess
import com.chenpeng.simpleToDo.stroe.net.BaseObserver
import com.chenpeng.simpleToDo.ui.base.DataBindingActivity
import com.chenpeng.simpleToDo.viewmodel.Live
import com.chenpeng.simpleToDo.viewmodel.UserViewModel

class RegisterActivity : DataBindingActivity<ActivityRegisterBinding>(), View.OnClickListener {

    private lateinit var viewModel: UserViewModel

    override fun layoutId(): Int {
        return R.layout.activity_register
    }

    override fun init(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        vdb.registerToolbar.setNavigationOnClickListener { finish() }
        vdb.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.btn_register -> {
                val phone = vdb.etPhone.text.toString().trim()
                val pwd = vdb.etPwd.text.toString().trim()
                val pwd2 = vdb.etPwd2.text.toString().trim()

                if (TextUtils.isEmpty(phone)) {
                    showError("手机号不能为空")
                    vdb.etPhone.requestFocus()
                    return
                }
                if (phone.length != 11) {
                    showError("手机号格式不正确")
                    vdb.etPhone.requestFocus()
                    return
                }
                if (TextUtils.isEmpty(pwd)) {
                    showError("密码不能为空")
                    vdb.etPwd.requestFocus()
                    return
                }
                if (TextUtils.isEmpty(pwd2)) {
                    showError("确认密码不能为空")
                    vdb.etPwd2.requestFocus()
                    return
                }
                if (!TextUtils.equals(pwd, pwd2)) {
                    showError("两次密码不一样")
                    return
                }
                register(phone, pwd)
            }
            else -> {
            }
        }
    }

    private fun register(phone: String, pwd: String) {
        viewModel.register(phone, pwd)
                .compose(Live.bindLifecycle(this))
                .subscribe(object : BaseObserver<BaseResult<Void>>() {
                    override fun onSuccess(baseResult: BaseResult<Void>) {
                        if (baseResult.code != BaseResult.SUCCESS) {
                            showError(baseResult.message)
                            return
                        }
                        showSuccess(baseResult.message)
                        finish()
                    }

                    override fun onFailure(e: Throwable) {
                    }

                })
    }

}