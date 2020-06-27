package com.gopal.todoapp

import android.app.*
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_task_.*
import kotlinx.android.synthetic.main.todo_adapter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
const val db_name="todo.db"
val labels= arrayListOf("Codeforces","Company","Codechef","InterviewBit","HackerRank","Gate","GFG","LeetCode","other")
class Task_Activity : AppCompatActivity() , View.OnClickListener{
    lateinit var myCalendar: Calendar
    lateinit var datesetListener:DatePickerDialog.OnDateSetListener
    lateinit var  timesetListener:TimePickerDialog.OnTimeSetListener
    var finalDate=0L
    var finalTime=0L


 val db by lazy{
  TodoDatabase.getDatabase(this)
 }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_)
     dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
        saveBtn.setOnClickListener(this)
       setSpinner()
    }


    override fun onClick(v: View) {
        when(v.id){
            R.id.dateEdt->{
            setlistener()
            }
            R.id.timeEdt->{
                setTimeListener()
            }
         R.id.saveBtn->{
             saveTask()
         }
        }
    }

    private fun saveTask() {
       var  category = spinnerCategory.selectedItem.toString()
        val title = titleInpLay.editText?.text.toString()
         var description = taskInpLay.editText?.text.toString()
        var id=0L
        GlobalScope.launch (Dispatchers.Main) {
            id = withContext(Dispatchers.IO) {
                return@withContext db.TodoDao().insertTask(
                    TodoTable(
                        title,
                        description,
                        category,
                        finalDate.toString(),
                        finalTime.toString()
                    )
                )
            }
        }
        val build= if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationCompat.Builder(this,"first")
        }
        else{
            NotificationCompat.Builder(this)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
        }
        val notfch=build.setContentTitle(category)
            .setContentText("$description $title at $finalTime")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        val i = Intent(this, NOTIFY::class.java);
        i.putExtra("Id",id.toInt())
        i.putExtra("Not",notfch)

        val pi = PendingIntent.getBroadcast(this, 123, i, PendingIntent.FLAG_ONE_SHOT)
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar: Calendar = Calendar.getInstance()
        am.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis, pi)
            finish()



    }



    private fun setSpinner() {
        val adapter=ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, labels)
        labels.sort()
        spinnerCategory.adapter=adapter
    }

    private fun setTimeListener() {
   myCalendar= Calendar.getInstance()
        timesetListener=TimePickerDialog.OnTimeSetListener{ _: TimePicker, hourOfday: Int, min: Int ->
            myCalendar.set(Calendar.HOUR_OF_DAY,hourOfday)
            myCalendar.set(Calendar.MINUTE,min)
            updateTime()
        }
        val timepickerDialog=TimePickerDialog(
            this,timesetListener, myCalendar.get(Calendar.HOUR_OF_DAY)
                    ,myCalendar.get(Calendar.MINUTE)
           ,false
        )

        timepickerDialog.show()
    }

    private fun updateTime() {
        val myformat="h:mm a"
        val sdf=SimpleDateFormat(myformat)
        finalTime = myCalendar.time.time
        timeEdt.setText(sdf.format(myCalendar.time))
    }

    private fun setlistener() {
        myCalendar= Calendar.getInstance()
        datesetListener=DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayofMonth: Int ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayofMonth)
         updateDate()
        }
        val datepickerDilog=DatePickerDialog(
                this,datesetListener,myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)
                )
        datepickerDilog.datePicker.minDate=System.currentTimeMillis()
        datepickerDilog.show()
    }

    private fun updateDate() {
        val myformat="EEE,d MMM YYYY"
        val sdf=SimpleDateFormat(myformat)
        finalDate = myCalendar.time.time
        dateEdt.setText(sdf.format(myCalendar.time))
    timeInptLay.visibility=View.VISIBLE
    }





}
