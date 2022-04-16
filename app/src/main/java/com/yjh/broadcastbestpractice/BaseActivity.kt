package com.yjh.broadcastbestpractice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/*
* 在BaseActivity中动态注册BroadcastReceiver
* */
open class BaseActivity: AppCompatActivity() {

    private lateinit var forceOffineReceiver: ForceOffineReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCollector.addActivity(this)
    }

    override fun onResume() {
        super.onResume()
        //动态注册接收广播
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.broadcastbestpractice.FORCE_OFFLINE")
        forceOffineReceiver = ForceOffineReceiver()
        registerReceiver(forceOffineReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(forceOffineReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    inner class ForceOffineReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "ForceOffline is worked", Toast.LENGTH_SHORT).show()
            AlertDialog.Builder(context!!).apply {
                setTitle("Warning")
                setMessage("You are forced to be offline. Please try to login again.")
                setCancelable(false)
                setPositiveButton("OK"){_, _->
                    ActivityCollector.finishAll() //销毁所有Activity
                    context.startActivity(Intent(context, LoginActivity::class.java)) //重新启动LoginActivity
                }
                show()
            }
        }
    }
}