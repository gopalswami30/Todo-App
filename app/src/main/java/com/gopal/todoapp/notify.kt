package com.gopal.todoapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class NOTIFY: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val cannel=     NotificationChannel(
                "first",
                "default",
                NotificationManager.IMPORTANCE_HIGH
            )
            cannel.apply {
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(
                cannel
            )}
        val notification: Notification =
            intent!!.getParcelableExtra("Not")
        val id = intent.getIntExtra("Id", 0)
        notificationManager.notify(id, notification)

    }}

