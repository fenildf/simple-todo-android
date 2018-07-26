package com.chenpeng.simpleToDo.ui

import android.os.Bundle
import android.os.Process
import android.support.design.widget.NavigationView
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.chenpeng.simpleToDo.App
import com.chenpeng.simpleToDo.R
import com.chenpeng.simpleToDo.databinding.ActivityMainBinding
import com.chenpeng.simpleToDo.navTo
import com.chenpeng.simpleToDo.stroe.db.LocalStore
import com.chenpeng.simpleToDo.ui.base.DataBindingActivity

class MainActivity : DataBindingActivity<ActivityMainBinding>(),
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private lateinit var tv_login: TextView

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        tv_login = vdb.navView.getHeaderView(0).findViewById(R.id.tv_login)

        tv_login.setOnClickListener(this)
        vdb.navView.setNavigationItemSelectedListener(this)

        vdb.navView.setCheckedItem(R.id.not_finished_todo)

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, UnFinishedFragment.newInstance())
                .commitNow()
    }

    override fun onResume() {
        super.onResume()
        when (App.get().isLogin) {
            true -> {
                vdb.navView.menu.findItem(R.id.exit).isVisible = true
                tv_login.text = App.get().user?.phone
            }
            else -> {
                if (App.get().user == null) {
                    tv_login.text = "登录"
                } else {
                    vdb.navView.menu.findItem(R.id.exit).isVisible = true
                    tv_login.text = App.get().user?.phone
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.not_finished_todo -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content, UnFinishedFragment.newInstance())
                        .commit()
                vdb.drawerLayout.closeDrawers()
            }
            R.id.finished_todo -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content, FinishedFragment.newInstance())
                        .commit()
                vdb.drawerLayout.closeDrawers()
            }
            R.id.exit -> {
                AlertDialog.Builder(this@MainActivity)
                        .setTitle("登出")
                        .setMessage("退出登录将删除本地用户信息")
                        .setPositiveButton("确定", { dialog, which ->
                            App.get().user?.let { LocalStore.deleteUser(it) }
                            Process.killProcess(Process.myPid())
                        })
                        .setNegativeButton("取消", null)
                        .show()

            }
        }
        return true
    }

    override fun onClick(v: View?) {
        if (v == null) return

        when (v.id) {
            R.id.tv_login -> {
                navTo(LoginActivity::class.java)
            }
            else -> {
            }
        }
    }

    fun openDrawer() {
        vdb.drawerLayout.openDrawer(Gravity.START)
    }

}
