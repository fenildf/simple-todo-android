package com.chenpeng.simpleToDo.dao

import android.arch.persistence.room.*
import com.chenpeng.simpleToDo.entities.UserBean

@Dao
interface UserDao {

    @Query("Select * from tb_user where id=:id")
    fun selectUser(id: Int): UserBean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserBean)

    @Delete
    fun delete(user: UserBean)
}