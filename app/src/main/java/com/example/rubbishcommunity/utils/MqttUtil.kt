package com.example.rubbishcommunity.utils

import android.content.Context
import com.example.rubbishcommunity.BuildConfig
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.persistence.getLocalEmail
import io.reactivex.Single
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.MqttConnectOptions


fun initMqttClient(applicationContext: Context): MqttAndroidClient {
	val serverURI = BuildConfig.MQTT_URL
	val clientId = "Android_${getLocalEmail()}"
	val mClient = MqttAndroidClient(applicationContext, serverURI, clientId)
	
	mClient.setCallback(object : MqttCallbackExtended {
		
		override fun connectComplete(reconnect: Boolean, serverURI: String) {
		
		}
		
		override fun connectionLost(cause: Throwable) {
		
		}
		
		override fun messageArrived(topic: String, message: MqttMessage) {
			MyApplication.showSuccess("MQTT消息：$topic : $message")
		}
		
		override fun deliveryComplete(token: IMqttDeliveryToken) {
		
		}
	})
	
/*	//配置失联参数
	val disconnectedBufferOptions = DisconnectedBufferOptions()
	disconnectedBufferOptions.isBufferEnabled = true
	disconnectedBufferOptions.bufferSize = 5000
	disconnectedBufferOptions.isDeleteOldestMessages = true
	disconnectedBufferOptions.isPersistBuffer = true
	mClient.setBufferOpts(disconnectedBufferOptions)*/
	
	return mClient
}


private fun getConnectOptions(): MqttConnectOptions {
	val options = MqttConnectOptions()
	options.isCleanSession = true
	options.userName = BuildConfig.MQTT_USERNAME
	options.password = BuildConfig.MQTT_PASSWORD.toCharArray()
	options.isAutomaticReconnect = true
	return options
}


//连接
fun mqttConnect(): Single<IMqttToken> {
	
	return Single.create { emitter ->
		MyApplication.mqttClient.connect(getConnectOptions(), object : IMqttActionListener {
			override fun onSuccess(asyncActionToken: IMqttToken) {
				emitter.onSuccess(asyncActionToken)
			}
			
			override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable?) {
				emitter.onError(exception ?: UnknownError())
			}
			
		})
	}
	
	
}

//订阅
fun mqttSubscribe(): Single<IMqttToken> {
	return Single.create { emitter ->
		MyApplication.mqttClient.subscribe("SystemDec", 1, null, object : IMqttActionListener {
			override fun onSuccess(asyncActionToken: IMqttToken) {
				emitter.onSuccess(asyncActionToken)
			}
			
			override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable?) {
				emitter.onError(exception ?: UnknownError())
			}
			
		})
	}
	
	
}


//发布消息
fun mqttPublish(): Single<IMqttToken> {
	
	return Single.create { emitter ->
		MyApplication.mqttClient.publish(
			"SystemDec",
			"Hello~Android".toByteArray(),
			1,
			false,
			null,
			object : IMqttActionListener {
				override fun onSuccess(asyncActionToken: IMqttToken) {
					emitter.onSuccess(asyncActionToken)
				}
				
				override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable?) {
					emitter.onError(exception ?: UnknownError())
				}
				
			})
	}
	
	
}


//取消订阅

fun unsubscribe(): Single<IMqttToken> {
	return Single.create { emitter ->
		MyApplication.mqttClient.unsubscribe("topic", null, object : IMqttActionListener {
			override fun onSuccess(asyncActionToken: IMqttToken) {
				emitter.onSuccess(asyncActionToken)
			}
			override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable?) {
				emitter.onError(exception ?: UnknownError())
			}
		})
	}
}


//断开连接
fun disconnect(): Single<IMqttToken> {
	return Single.create { emitter ->
		MyApplication.mqttClient.disconnect(null, object : IMqttActionListener {
			override fun onSuccess(asyncActionToken: IMqttToken) {
				emitter.onSuccess(asyncActionToken)
			}
			
			override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable?) {
				emitter.onError(exception ?: UnknownError())
			}
			
		})
	}
	
}