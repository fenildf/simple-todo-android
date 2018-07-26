package com.chenpeng.simpleToDo.stroe

import com.chenpeng.simpleToDo.stroe.net.ApiFactory

/**
 * author : ChenPeng
 * date : 2018/4/13
 * description :
 */
class Repository {

    companion object {

        fun addTodo(u_id: Int, content: String) {
            ApiFactory.api().todo_add(u_id,content)
        }
    }
}