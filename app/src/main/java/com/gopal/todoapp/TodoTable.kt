package com.gopal.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class TodoTable(
    var title:String,
    var descrp:String,
    var category:String,
    var date:String,
    var time:String,
    var isFinished:Int=0,
     @PrimaryKey(autoGenerate = true)
    var id:Long=0L
)