package com.example.rubbishcommunity.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
class MqServiceConnection :ServiceConnection{
	private lateinit var mqService: MyMqttService
	
	override fun onServiceDisconnected(p0: ComponentName?) {
	
	}
	
	override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
		mqService = (iBinder as MyMqttService.CustomBinder).service
	}
	
	fun getMqttService(): MyMqttService {
		return mqService
	}

	
}