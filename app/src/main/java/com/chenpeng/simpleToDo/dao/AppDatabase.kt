package com.chenpeng.simpleToDo.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.chenpeng.simpleToDo.entities.UserBean

@Database(entities = arrayOf(UserBean::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}