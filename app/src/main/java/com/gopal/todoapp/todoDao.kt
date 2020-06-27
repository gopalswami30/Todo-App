package com.gopal.todoapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface  todoDao{
    @Insert
    suspend fun insertTask(todoTable: TodoTable):Long
    @Query("select *from todotable where isFinished==0")
    fun getTask():LiveData<List<TodoTable>>
    @Query("update todotable set isFinished=1 where id=:uid")
    fun finishTask(uid:Long)
    @Query("Delete from TodoTable where id=:uid")
    fun deleteTask(uid:Long)
}