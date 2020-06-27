package com.gopal.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_adapter.view.*
import java.text.SimpleDateFormat
import java.util.*


class TodoAdapter(val list:List<TodoTable>):RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){
     class TodoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(todoTable: TodoTable) {
        with(itemView){
            val color=resources.getIntArray(R.array.random_color)
            val rancolor=color[Random().nextInt(color.size)]
            viewColorTag.setBackgroundColor(rancolor)
            txtShowTitle.text=todoTable.title
            txtShowTask.text=todoTable.descrp
            txtShowCategory.text=todoTable.category
            updateTime(todoTable.time.toLong())
            updateDate(todoTable.date.toLong())
        }
        }

        private fun updateTime(time:Long) {
            val myformat="h:mm a"
            val sdf=SimpleDateFormat(myformat)
            itemView.txtShowTime.text=sdf.format(Date(time))
        }
        private fun updateDate(time:Long) {
            val myformat="EEE,d MMM yyyy"
            val sdf=SimpleDateFormat(myformat)
            itemView.txtShowDate.text=sdf.format(Date(time))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoViewHolder {
    return TodoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.todo_adapter,parent,false))
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }
    override fun getItemCount()=list.size

    override fun onBindViewHolder(holder: TodoAdapter.TodoViewHolder, position: Int) {
    holder.bind(list[position])
    }

}