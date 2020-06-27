package com.gopal.todoapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities =[TodoTable::class],version = 1 )
abstract class TodoDatabase:RoomDatabase(){

    abstract fun TodoDao():todoDao

    companion object{
        @Volatile
        private var INSTANCE:TodoDatabase?=null
        fun getDatabase(context:Context):TodoDatabase{
            val tempInstance= INSTANCE
            if(tempInstance!=null)
                return tempInstance
            synchronized(this){
                val instance= Room.databaseBuilder(context.applicationContext,
                    TodoDatabase::class.java,
                db_name
                    ).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}