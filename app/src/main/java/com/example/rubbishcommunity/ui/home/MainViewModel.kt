package com.example.rubbishcommunity.ui.home

import android.app.Application
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.utils.mqttConnect
import com.example.rubbishcommunity.utils.mqttSubscribe
import com.example.rubbishcommunity.utils.setDisconnectedBufferOptions


class MainViewModel(application: Application) : BaseViewModel(application) {
	
	fun init() {
		mqttConnect()
			.doOnSuccess { MyApplication.showSuccess("MQTT连接成功") }
			.flatMap {
				mqttSubscribe()
			}.doOnSuccess {
				MyApplication
					.showSuccess("MQTT订阅成功")
				setDisconnectedBufferOptions()
			}
			.bindLife()
	}
	
}