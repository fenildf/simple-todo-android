package com.chenpeng.simpleToDo.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.chenpeng.simpleToDo.*
import com.chenpeng.simpleToDo.databinding.ActivityLoginBinding
import com.chenpeng.simpleToDo.entities.BaseResult
import com.chenpeng.simpleToDo.entities.UserBean
import com.chenpeng.simpleToDo.stroe.db.LocalStore
import com.chenpeng.simpleToDo.stroe.net.BaseObserver
import com.chenpeng.simpleToDo.ui.base.DataBindingActivity
import com.chenpeng.simpleToDo.utils.EventBusUtils
import com.chenpeng.simpleToDo.utils.MessageEvent
import com.chenpeng.simpleToDo.viewmodel.Live
import com.chenpeng.simpleToDo.viewmodel.UserViewModel

class LoginActivity : DataBindingActivity<ActivityLoginBinding>(), View.OnClickListener {

    private lateinit var vm: UserViewModel

    override fun layoutId(): Int {
        return R.layout.activity_login
    }

    override fun init(savedInstanceState: Bundle?) {
        vm = ViewModelProviders.of(this).get(UserViewModel::class.java)

        vdb.btnLogin.setOnClickListener(this)
        vdb.loginToolbar.setNavigationOnClickListener { finish() }
        setSupportActionBar(vdb.loginToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        return true
    }

    val REQUEST_CODE = 111

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_register -> {
                startActivityForResult(Intent(this@LoginActivity,
                        RegisterActivity::class.java), REQUEST_CODE)
            }
            else -> {
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.btn_login -> {
                val phone = vdb.etPhone.text.toString().trim()
                val pwd = vdb.etPwd.text.toString().trim()
                if (TextUtils.isEmpty(phone)) {
                    showError("请输入手机号")
                    vdb.etPhone.requestFocus()
                    return
                }
                if (phone.length != 11) {
                    showError("手机号格式不对")
                    vdb.etPhone.requestFocus()
                    return
                }
                if (TextUtils.isEmpty(pwd)) {
                    showError("请输入密码")
                    vdb.etPwd.requestFocus()
                    return
                }
                login(phone, pwd)
            }
            else -> {
            }
        }
    }

    private fun login(phone: String, pwd: String) {
        vm.login(phone, pwd)
                .compose(Live.bindLifecycle<BaseResult<UserBean>>(this))
                .subscribe(object : BaseObserver<BaseResult<UserBean>>() {
                    override fun onSuccess(baseResult: BaseResult<UserBean>) {
                        if (baseResult.code != BaseResult.SUCCESS) {
                            showError(baseResult.message)
                            return
                        }
                        if (baseResult.result == null) {
                            toast("用户信息为空")
                            return
                        }
                        App.get().user = baseResult.result
                        toast(baseResult.message)
                        EventBusUtils.post(MessageEvent.REFRESH_TODO)
                        navTo(MainActivity::class.java)

                        baseResult.result?.let {
                            LocalStore.saveUser(userBean = it)
                            App.get().isLogin = true
                        }
                    }

                    override fun onFailure(e: Throwable) {
                    }

                })
    }
}