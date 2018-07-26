package com.chenpeng.simpleToDo.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chenpeng.simpleToDo.App
import com.chenpeng.simpleToDo.R
import com.chenpeng.simpleToDo.databinding.FragmentUnfinishedBinding
import com.chenpeng.simpleToDo.entities.BaseResult
import com.chenpeng.simpleToDo.entities.ToDoBean
import com.chenpeng.simpleToDo.showError
import com.chenpeng.simpleToDo.stroe.net.BaseObserver
import com.chenpeng.simpleToDo.toast
import com.chenpeng.simpleToDo.ui.adapter.UnFinishedAdapter
import com.chenpeng.simpleToDo.ui.base.DataBindingFragment
import com.chenpeng.simpleToDo.utils.EventBusUtils
import com.chenpeng.simpleToDo.utils.MessageEvent
import com.chenpeng.simpleToDo.viewmodel.Live
import com.chenpeng.simpleToDo.viewmodel.TodoViewModel
import com.chenpeng.simpleToDo.widget.CpBottomSheetDialog
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class UnFinishedFragment : DataBindingFragment<FragmentUnfinishedBinding>(),
        View.OnClickListener, OnRefreshLoadMoreListener {

    companion object {
        const val REQUEST_CODE = 111

        fun newInstance() = UnFinishedFragment().apply { }
    }

    private lateinit var viewModel: TodoViewModel

    private var start = 0
    private var count = 5
    private val mData: ArrayList<ToDoBean> = ArrayList()
    private var adapter: UnFinishedAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusUtils.registet(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_unfinished
    }

    override fun init(view: View?, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)

        view?.let { initView(it) }
        refresh()
    }

    private fun initView(view: View) {
        vdb.toolbar.setNavigationOnClickListener { (activity as MainActivity).openDrawer() }
        vdb.fabAddNew.setOnClickListener(this)
        vdb.refreshLayout.setOnRefreshLoadMoreListener(this)

        adapter = UnFinishedAdapter(activity!!, R.layout.item_todo, mData)
        adapter?.setOnFinishTodoCheckedListener(object : UnFinishedAdapter.OnFinishTodoCheckedListener {
            override fun onChecked(todo: ToDoBean, position: Int, isChecked: Boolean) {
                if (isChecked) {
                    finishTodo(todo, position)
                }
            }
        })

        vdb.rvUnFinished.addItemDecoration(StickyRecyclerHeadersDecoration(adapter))
        vdb.rvUnFinished.addItemDecoration(DividerItemDecoration(activity,
                LinearLayoutManager.VERTICAL))
        vdb.rvUnFinished.adapter = adapter
        vdb.rvUnFinished.layoutManager = LinearLayoutManager(activity)
        adapter?.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?,
                                         position: Int): Boolean {
                return true
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                popMoreDialog(mData[holder?.adapterPosition!!], holder.adapterPosition)
            }

        })
    }

    private fun refresh() {
        start = 0
        viewModel.list(App.get().user?.u_id, start, count)
                .compose(Live.bindLifecycle(this))
                .subscribe(object : BaseObserver<BaseResult<List<ToDoBean>>>() {
                    override fun onSuccess(baseResult: BaseResult<List<ToDoBean>>) {
                        vdb.refreshLayout.finishRefresh()
                        if (baseResult.code != BaseResult.SUCCESS) {
                            activity?.showError(baseResult.message)
                            return
                        }
                        baseResult.result?.let {
                            mData.clear()
                            start += count
                            mData.addAll(baseResult.result!!)
                            adapter?.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        vdb.refreshLayout.finishRefresh()
                    }

                })
    }

    private fun loadMore() {
        viewModel.list(App.get().user?.u_id, start, count)
                .compose(Live.bindLifecycle(this))
                .subscribe(object : BaseObserver<BaseResult<List<ToDoBean>>>() {
                    override fun onSuccess(baseResult: BaseResult<List<ToDoBean>>) {
                        vdb.refreshLayout.finishLoadMore()
                        if (baseResult.code != BaseResult.SUCCESS) {
                            activity?.showError(baseResult.message)
                            return
                        }
                        if (baseResult.result == null || baseResult.result?.isEmpty()!!) {
                            activity?.toast("已无数据")
                        } else {
                            start += count
                            mData.addAll(baseResult.result!!)
                            adapter?.notifyDataSetChanged()
                        }

                    }

                    override fun onFailure(e: Throwable) {
                        vdb.refreshLayout.finishLoadMore()
                    }

                })
    }


    private fun finishTodo(t: ToDoBean, adapterPosition: Int) {
        viewModel.finish(App.get().user?.u_id, t.id!!)
                ?.subscribe(object : BaseObserver<BaseResult<Void>>() {
                    override fun onSuccess(baseResult: BaseResult<Void>) {
                        if (baseResult.code != BaseResult.SUCCESS) {
                            activity?.toast(baseResult.message)
                            return
                        }
                        adapter?.let {
                            mData.remove(t)
                            it.notifyItemRemoved(adapterPosition)
                        }
                    }

                    override fun onFailure(e: Throwable) {
                    }

                })
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.fab_addNew -> {
                startActivityForResult(Intent(activity, AddNewActivity::class.java), REQUEST_CODE)
            }
            else -> {
            }
        }
    }

    private fun popMoreDialog(todo: ToDoBean, position: Int) {
//        activity?.toast("popMoreDialog")
        activity?.let {
            val dialog = CpBottomSheetDialog(it)
            dialog.setItemData(arrayOf("修改", "删除", "取消"))
            dialog.show()
            dialog.setOnItemClickListener(object : CpBottomSheetDialog.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    activity?.toast("" + position)
                    when (position) {
                        0 -> {//修改
                        }
                        1 -> {//删除
                            deleteTodo(todo, position)
                        }
                        2 -> {//取消
                            dialog.cancel()
                        }
                        else -> {
                        }
                    }
                }
            })
        }
    }

    private fun deleteTodo(todo: ToDoBean, position: Int) {
        if (todo == null || todo.id == null) {
            return
        }
        viewModel.delete(App.get().user?.id, todo.id)
                ?.subscribe(object : BaseObserver<BaseResult<Void>>() {
                    override fun onSuccess(baseResult: BaseResult<Void>) {
                        if (baseResult.isFailure()) {
                            activity?.toast(baseResult.message)
                            return
                        }
                        activity?.toast(baseResult.message)
                        mData.remove(todo)
                        adapter?.notifyItemRemoved(position)

                    }

                    override fun onFailure(e: Throwable) {
                    }

                })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != REQUEST_CODE || resultCode != Activity.RESULT_OK ||
                data == null) {
            return
        }

        val todo = data.getSerializableExtra("todo") as ToDoBean
        mData.add(0, todo)
        adapter?.notifyItemInserted(0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        when (event.what) {
            MessageEvent.REFRESH_TODO -> {
                refresh()
            }
            else -> {
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore()
    }
}