package com.chenpeng.simpleToDo.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chenpeng.simpleToDo.App
import com.chenpeng.simpleToDo.R
import com.chenpeng.simpleToDo.databinding.FragmentFinishedBinding
import com.chenpeng.simpleToDo.entities.BaseResult
import com.chenpeng.simpleToDo.entities.ToDoBean
import com.chenpeng.simpleToDo.stroe.net.BaseObserver
import com.chenpeng.simpleToDo.toast
import com.chenpeng.simpleToDo.ui.adapter.FinishedAdapter
import com.chenpeng.simpleToDo.ui.base.DataBindingFragment
import com.chenpeng.simpleToDo.utils.ToastUtils
import com.chenpeng.simpleToDo.viewmodel.Live
import com.chenpeng.simpleToDo.viewmodel.TodoViewModel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration

/**
 * author : ChenPeng
 * date : 2018/4/13
 * description :
 */
class FinishedFragment : DataBindingFragment<FragmentFinishedBinding>(), OnRefreshLoadMoreListener {

    companion object {
        fun newInstance() = FinishedFragment().apply {

        }
    }

    private lateinit var viewModel: TodoViewModel
    private val mData: ArrayList<ToDoBean> = ArrayList()
    private lateinit var adapter: FinishedAdapter
    private var start = 0
    private var count = 5

    override fun layoutId(): Int {
        return R.layout.fragment_finished
    }

    override fun init(view: View?, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        view?.let { initView(it) }
        refresh()
    }

    private fun initView(view: View) {
        vdb.toolbarFinished.setNavigationOnClickListener { (activity as MainActivity).openDrawer() }
        vdb.refreshLayout.setOnRefreshLoadMoreListener(this)
        vdb.rvFinished.layoutManager = LinearLayoutManager(activity)

        vdb.rvFinished.addItemDecoration(DividerItemDecoration(activity,
                LinearLayoutManager.VERTICAL))
        adapter = FinishedAdapter(activity!!,R.layout.item_finished,mData)

        vdb.rvFinished.addItemDecoration(StickyRecyclerHeadersDecoration(adapter))
        vdb.rvFinished.addItemDecoration(DividerItemDecoration(activity,
                LinearLayoutManager.VERTICAL))
        vdb.rvFinished.adapter = adapter
    }

    private fun refresh() {
        start = 0
        viewModel.finished(App.get().user?.u_id, start, count)
                ?.compose(Live.bindLifecycle(this))
                ?.subscribe(object : BaseObserver<BaseResult<List<ToDoBean>>>() {
                    override fun onSuccess(baseResult: BaseResult<List<ToDoBean>>) {
                        vdb.refreshLayout.finishRefresh()
                        if (baseResult.code != BaseResult.SUCCESS) {
                            ToastUtils.show(App.get(), baseResult.message)
                            return
                        }
                        when (baseResult.result == null || baseResult.result?.isEmpty()!!) {
                            true -> {
                                activity?.toast("暂无数据")
                            }
                            false -> {
                                start += count
                                mData.clear()
                                mData.addAll(baseResult.result!!)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        vdb.refreshLayout.finishRefresh()
                    }

                })
    }

    private fun loadMore() {
        viewModel.finished(App.get().user?.u_id, start, count)
                ?.compose(Live.bindLifecycle(this))
                ?.subscribe(object : BaseObserver<BaseResult<List<ToDoBean>>>() {
                    override fun onSuccess(baseResult: BaseResult<List<ToDoBean>>) {
                        vdb.refreshLayout.finishRefresh()
                        if (baseResult.code != BaseResult.SUCCESS) {
                            ToastUtils.show(App.get(), baseResult.message)
                            return
                        }
                        when (baseResult.result == null || baseResult.result?.isEmpty()!!) {
                            true -> {
                                activity?.toast("暂无数据")
                            }
                            false -> {
                                start += count
                                mData.addAll(baseResult.result!!)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        vdb.refreshLayout.finishRefresh()
                    }

                })
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }
}