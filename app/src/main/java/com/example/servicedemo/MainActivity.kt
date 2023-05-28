package com.example.servicedemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.servicedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var boundService: BoundService? = null
    private var isBound: Boolean = false
    private lateinit var binding:ActivityMainBinding

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BoundService.LocalBinder
            boundService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            boundService = null
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBindService.setOnClickListener {
            val intent = Intent(this, BoundService::class.java)
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            startGeneratingRandomNumber()
        }
        binding.btnStopBindService.setOnClickListener {
            stopGeneratingRandomNumber()
        }
        binding.btnUpdateBindService.setOnClickListener {
            binding.edtGetValueService.text = boundService?.getRandomNumber().toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
    }

    private fun startGeneratingRandomNumber() {
        boundService?.startGeneratingRandomNumber()
    }

    private fun stopGeneratingRandomNumber() {
        boundService?.stopGeneratingRandomNumber()
    }
}
