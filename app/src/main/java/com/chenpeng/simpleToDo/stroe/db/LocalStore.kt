package com.chenpeng.simpleToDo.stroe.db

import com.chenpeng.simpleToDo.App
import com.chenpeng.simpleToDo.entities.UserBean

class LocalStore {

    companion object {

        fun saveUser(userBean: UserBean) {
            App.get().db?.userDao()?.insert(userBean)
        }

        fun getUser(): UserBean? {
            return App.get().db?.userDao()?.selectUser(0)
        }

        fun deleteUser(userBean: UserBean){
            App.get().db?.userDao()?.delete(userBean)
        }
    }
}