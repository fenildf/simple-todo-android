package com.chenpeng.simpleToDo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.chenpeng.simpleToDo.R
import com.chenpeng.simpleToDo.entities.ToDoBean
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import java.text.SimpleDateFormat
import java.util.*

class UnFinishedAdapter(context: Context, layoutId: Int, datas: List<ToDoBean>)
    : CommonAdapter<ToDoBean>(context, layoutId, datas),
        StickyRecyclerHeadersAdapter<ViewHolder> {

    override fun convert(holder: ViewHolder?, t: ToDoBean?, position: Int) {
        if (holder == null || t == null) {
            return
        }

        val tv_text = holder.getView<TextView>(R.id.tv_text)
        val cb_finish = holder.getView<CheckBox>(R.id.cb_finish)

        tv_text?.text = t.content
        cb_finish.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                mListener?.onChecked(t, holder.adapterPosition, isChecked)
            }
        }

    }

    interface OnFinishTodoCheckedListener {
        fun onChecked(todo: ToDoBean, position: Int, isChecked: Boolean)
    }

    private var mListener: OnFinishTodoCheckedListener? = null

    fun setOnFinishTodoCheckedListener(listener: OnFinishTodoCheckedListener) {
        mListener = listener
    }

    override fun getHeaderId(position: Int): Long {
        val createTime = mDatas[position].create_time?.substring(0, 10)
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(createTime)
        return calendar.timeInMillis
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_header, parent, false)
        return ViewHolder(parent?.context, view)
    }

    override fun onBindHeaderViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return

        val createTime = mDatas[position].create_time?.substring(0, 10)
        holder.setText(R.id.tv_headerName, createTime)
    }

}