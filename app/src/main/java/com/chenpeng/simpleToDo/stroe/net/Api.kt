package com.chenpeng.simpleToDo.stroe.net

import com.chenpeng.simpleToDo.entities.BaseResult
import com.chenpeng.simpleToDo.entities.ToDoBean
import com.chenpeng.simpleToDo.entities.UserBean
import io.reactivex.Observable
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("user/login")
    fun user_login(@Field("phone") phone: String, @Field("pwd") pwd: String)
            : Observable<BaseResult<UserBean>>

    @FormUrlEncoded
    @POST("user/register")
    fun user_register(@Field("phone") phone: String, @Field("pwd") pwd: String)
            : Observable<BaseResult<Void>>

    @FormUrlEncoded
    @POST("todo/add")
    fun todo_add(@Field("u_id") u_id: Int, @Field("content") content: String)
            : Observable<BaseResult<ToDoBean>>

    @FormUrlEncoded
    @POST("todo/delete")
    fun todo_delete(@Field("u_id") u_id: Int, @Field("todo_id") todo_id: Int)
            : Observable<BaseResult<Void>>

    @FormUrlEncoded
    @POST("todo/finish")
    fun todo_finish(@Field("u_id") u_id: Int, @Field("todo_id") todo_id: Int)
            : Observable<BaseResult<Void>>

    @GET("todo/list")
    fun todo_list(@Query("u_id") u_id: Int,
                  @Query("start") start: Int,
                  @Query("count") count: Int)
            : Observable<BaseResult<List<ToDoBean>>>

    @GET("todo/finished")
    fun todo_finished(@Query("u_id") u_id: Int,
                      @Query("start") start: Int,
                      @Query("count") count: Int)
            : Observable<BaseResult<List<ToDoBean>>>

    @GET("todo/deleted")
    fun todo_deleted(@Field("u_id") u_id: Long)
            : Observable<BaseResult<List<ToDoBean>>>
}