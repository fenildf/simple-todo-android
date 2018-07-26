package com.chenpeng.simpleToDo.widget

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.chenpeng.simpleToDo.R
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

class CpBottomSheetDialog(context: Context) : BottomSheetDialog(context) {

    private var dialogView: View = LayoutInflater.from(context)
            .inflate(R.layout.dialog_bottom_sheet_cp, null)
    private lateinit var adapter: CommonAdapter<String>

    init {

    }

    fun setItemData(items: Array<String>) {
        setContentView(dialogView)
        val rvBottomSheet = dialogView.findViewById<RecyclerView>(R.id.rv_bottomSheet)
        rvBottomSheet.addItemDecoration(DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL))
        adapter = object : CommonAdapter<String>(context, R.layout.item_sheet_dialog,
                items.toMutableList()) {
            override fun convert(holder: ViewHolder?, item: String?, position: Int) {
                if (holder == null || item == null) return

                holder.setText(R.id.tv_itemName, item)
            }
        }

        rvBottomSheet.layoutManager = LinearLayoutManager(context)
        rvBottomSheet.adapter = adapter
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                onItemClickListener.onItemClick(position)
                this@CpBottomSheetDialog.cancel()
            }

            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

        })
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}