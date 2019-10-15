package com.example.rubbishcommunity.ui.home

import android.app.Application
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.main.mainModule
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.utils.*
import io.reactivex.Single
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.instance


class MainViewModel(application: Application) : BaseViewModel(application) {
	
	private val _parentKodein: Kodein by lazy { (application as MyApplication).kodein }
	
	override val kodein = Kodein {
		extend(_parentKodein, copy = Copy.All)
		import(mainModule)
	}
	
	val mqttClient by instance<MqttAndroidClient>()
	
	fun initMqtt(): Single<IMqttToken>{
		return mqttConnect(mqttClient)
			.doOnSuccess { MyApplication.showSuccess("MQTT连接成功") }
			.flatMap {
				mqttSubscribe(mqttClient)
			}.doOnSuccess {
				MyApplication
					.showSuccess("MQTT订阅成功")
				setDisconnectedBufferOptions(mqttClient)
			}
	}
	
}