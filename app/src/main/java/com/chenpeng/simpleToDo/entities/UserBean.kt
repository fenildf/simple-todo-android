package com.chenpeng.simpleToDo.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "tb_user")
class UserBean {

    @Expose
    @PrimaryKey()
    var id: Int = 0

    @ColumnInfo(name = "u_id")
    var u_id: Int? = null

    @ColumnInfo(name = "phone")
    var phone: String = ""
}