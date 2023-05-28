package com.example.servicedemo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class BoundService : Service() {
    private val binder = LocalBinder()
    private var randomNumber: Int = 0
    private var isGeneratingRandomNumber: Boolean = false
    private val random = Random()

    inner class LocalBinder : Binder() {
        fun getService(): BoundService = this@BoundService
    }

    fun getRandomNumber(): Int {
        return randomNumber
    }

    fun startGeneratingRandomNumber() {
        if (!isGeneratingRandomNumber) {
            isGeneratingRandomNumber = true
            generateRandomNumber()
        }
    }

    fun stopGeneratingRandomNumber() {
        isGeneratingRandomNumber = false
    }

    private fun generateRandomNumber() {
        CoroutineScope(Dispatchers.IO).launch {
            while (isGeneratingRandomNumber) {
                randomNumber = random.nextInt(100)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        stopGeneratingRandomNumber()
    }
}
