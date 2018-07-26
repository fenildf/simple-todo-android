package com.chenpeng.simpleToDo.utils

import android.content.Context
import android.widget.Toast

class ToastUtils {

    companion object {
        fun show(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

}