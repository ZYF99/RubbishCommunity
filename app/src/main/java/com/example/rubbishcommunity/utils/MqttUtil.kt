package com.example.rubbishcommunity.utils


import android.content.Context
import com.example.rubbishcommunity.BuildConfig
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.persistence.getLocalEmail
import com.example.rubbishcommunity.ui.utils.sendSimpleNotification
import io.reactivex.Single
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.MqttConnectOptions


fun initMqttClient(applicationContext: Context): MqttAndroidClient {
	val serverURI = BuildConfig.MQTT_URL
	val clientId = "Android_${getLocalEmail()}"
	val mqttClient = MqttAndroidClient(applicationContext, serverURI, clientId)
	
	mqttClient.setCallback(object : MqttCallbackExtended {
		
		override fun connectComplete(reconnect: Boolean, serverURI: String) {
		
		}
		
		override fun connectionLost(cause: Throwable) {
			MyApplication.showWarning("MQTT断开连接：${cause.message}")
			mqttConnect(mqttClient)
			
		}
		
		override fun messageArrived(topic: String, message: MqttMessage) {
			
			sendSimpleNotification(applicationContext, topic, message.toString())
		}
		
		override fun deliveryComplete(token: IMqttDeliveryToken) {
		
		}
	})
	
	return mqttClient
}

//配置失联参数
fun setDisconnectedBufferOptions(mqttClient: MqttAndroidClient) {
	val disconnectedBufferOptions = DisconnectedBufferOptions()
	disconnectedBufferOptions.isBufferEnabled = true
	disconnectedBufferOptions.bufferSize = 5000
	disconnectedBufferOptions.isDeleteOldestMessages = true
	disconnectedBufferOptions.isPersistBuffer = true
	mqttClient.setBufferOpts(disconnectedBufferOptions)
}

//获取连接参数
private fun getConnectOptions(): MqttConnectOptions {
	val options = MqttConnectOptions()
	options.isCleanSession = true
	options.userName = BuildConfig.MQTT_USERNAME
	options.password = BuildConfig.MQTT_PASSWORD.toCharArray()
	options.isAutomaticReconnect = true
	return options
}


//连接
fun mqttConnect(mqttClient: MqttAndroidClient): Single<IMqttToken> {
	
	return Single.create { emitter ->
		mqttClient.connect(getConnectOptions(), object : IMqttActionListener {
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
fun mqttSubscribe(mqttClient: MqttAndroidClient): Single<IMqttToken> {
	return Single.create { emitter ->
		mqttClient.subscribe("SystemDec", 1, null, object : IMqttActionListener {
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
fun mqttPublish(mqttClient: MqttAndroidClient): Single<IMqttToken> {
	
	return Single.create { emitter ->
		mqttClient.publish(
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
fun mqttUnsubscribe(mqttClient: MqttAndroidClient): Single<IMqttToken> {
	return Single.create { emitter ->
		mqttClient.unsubscribe("SystemDec", null, object : IMqttActionListener {
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
fun mqttDisconnect(mqttClient: MqttAndroidClient): Single<IMqttToken> {
	return Single.create { emitter ->
		mqttClient.disconnect(null, object : IMqttActionListener {
			override fun onSuccess(asyncActionToken: IMqttToken) {
				emitter.onSuccess(asyncActionToken)
			}
			
			override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable?) {
				emitter.onError(exception ?: UnknownError())
			}
			
		})
	}
	
}