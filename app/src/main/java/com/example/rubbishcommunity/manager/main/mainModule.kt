package com.example.rubbishcommunity.manager.main

import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.utils.initMqttClient
import org.eclipse.paho.android.service.MqttAndroidClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val mainModule = Kodein.Module(){
	
	bind<MqttAndroidClient>() with singleton { initMqttClient(MyApplication.instance) }
	
	
}




