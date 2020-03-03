package com.example.rubbishcommunity.service

import android.app.*
import android.content.Context
import org.eclipse.paho.client.mqttv3.MqttException
import android.content.Intent
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.*
import androidx.annotation.RequiresApi
import com.example.rubbishcommunity.ui.splash.SplashActivity
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.android.service.MqttAndroidClient
import androidx.core.app.NotificationCompat
import com.example.rubbishcommunity.BuildConfig
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.persistence.getLocalEmail
import com.example.rubbishcommunity.persistence.getLocalUin
import com.example.rubbishcommunity.ui.utils.sendSimpleNotification

/**
 * @author Zhangyf
 */

const val TAG = "nlgMqttService"
const val TOPIC_TO_QA = "/s2c/task_quality/"
const val CHANNEL_ID_STRING = "用于显示即使消息"
val PUBLISH_TOPIC = "UA:${getLocalUin()}"

class MyMqttService : Service() {
	private var mqttAndroidClient: MqttAndroidClient? = null
	
	inner class CustomBinder : Binder() {
		val service: MyMqttService
			get() = this@MyMqttService
	}
	
	override fun onBind(intent: Intent): IBinder {
		return CustomBinder()
	}
	
	override fun onCreate() {
		super.onCreate()
		//mqtt服务器的地址
		val serverUri = BuildConfig.MQTT_URL
		//新建Client,以设备ID作为client ID
		mqttAndroidClient =
			MqttAndroidClient(this@MyMqttService, serverUri, "UA:${getLocalUin()}")
		mqttAndroidClient?.setCallback(object : MqttCallbackExtended {
			override fun connectComplete(reconnect: Boolean, serverURI: String) {
				//连接成功
				if (reconnect) {
					// 由于clean Session ，我们需要重新订阅
					try {
						subscribeToTopic(PUBLISH_TOPIC)
					}catch (e:Exception){
					
					}
				}
			}
			
			override fun connectionLost(cause: Throwable) {
				//连接断开
			}
			
			override fun messageArrived(topic: String, message: MqttMessage) {
				//订阅的消息送达，推送notify
				
				sendSimpleNotification(applicationContext, topic, message.toString())
			}
			
			override fun deliveryComplete(token: IMqttDeliveryToken) {
				//即服务器成功delivery消息
			}
		})
		//新建连接设置
		val mqttConnectOptions = MqttConnectOptions()
		//断开后，是否自动连接
		mqttConnectOptions.isAutomaticReconnect = true
		//是否清空客户端的连接记录。若为true，则断开后，broker将自动清除该客户端连接信息
		mqttConnectOptions.isCleanSession = false
		//设置Mq连接的userName
		mqttConnectOptions.userName = getLocalEmail()
		//设置超时时间，单位为秒
		//mqttConnectOptions.setConnectionTimeout(2);
		//心跳时间，单位为秒。即多长时间确认一次Client端是否在线
		mqttConnectOptions.keepAliveInterval = 2
		//允许同时发送几条消息（未收到broker确认信息）
		//mqttConnectOptions.setMaxInflight(10)
		//选择MQTT版本
		mqttConnectOptions.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
		try {
			//开始连接
			mqttAndroidClient?.connect(mqttConnectOptions, this, object : IMqttActionListener {
				override fun onSuccess(asyncActionToken: IMqttToken) {
					val disconnectedBufferOptions = DisconnectedBufferOptions()
					disconnectedBufferOptions.isBufferEnabled = true
					disconnectedBufferOptions.bufferSize = 100
					disconnectedBufferOptions.isPersistBuffer = false
					disconnectedBufferOptions.isDeleteOldestMessages = false
					mqttAndroidClient!!.setBufferOpts(disconnectedBufferOptions)
					//成功连接以后开始订阅
					subscribeToTopic(PUBLISH_TOPIC)
				}
				
				override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
					//连接失败
					MyApplication.showWarning("MQTT连接失败:${exception.message}")
					exception.printStackTrace()
				}
			})
		} catch (ex: MqttException) {
			ex.printStackTrace()
			MyApplication.showWarning("MQTT异常：${ex.message}")
		}
		//适配8.0service
		startForeground()
	}
	
	private fun startForeground() {
		val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			createNotificationChannel()
		} else {
			//If earlier version channel ID is not used
			//https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
			"2"
		}
		//service绑定notification
		val intent = Intent(this, SplashActivity::class.java)
		//intent.putExtra(SinSimApp.FROM_NOTIFICATION, true)*/
		//这边设置“FLAG_UPDATE_CURRENT”是为了让后面的Activity接收pendingIntent中Extra的数据
		val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
		val notificationBuilder = NotificationCompat.Builder(this, channelId)
		val notification = notificationBuilder.setOngoing(true)
			.setContentTitle("美社MQ服务")
			.setContentText("正在为你更新信息～")
			.setSmallIcon(R.drawable.icon_find_nor)
			.setWhen(System.currentTimeMillis())
			.setPriority(Notification.PRIORITY_HIGH)
			.setCategory(Notification.CATEGORY_SERVICE)
			.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_find_nor))
			.setContentIntent(pi)
			.build()
		startForeground(101, notification)
	}
	
	@RequiresApi(Build.VERSION_CODES.O)
	private fun createNotificationChannel(): String {
		val channelId = "my_service"
		val channelName = "My Background Service"
		val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
		chan.lightColor = Color.BLUE
		chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
		val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		service.createNotificationChannel(chan)
		return channelId
	}
	
	
	//订阅消息
	fun subscribeToTopic(subscriptionTopic: String) {
		try {
			mqttAndroidClient?.subscribe(subscriptionTopic, 1, "1", object : IMqttActionListener {
				override fun onSuccess(asyncActionToken: IMqttToken) {
				}
				
				override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
				}
			})
		} catch (ex: MqttException) {
			ex.printStackTrace()
		}
	}
	
	//发布消息
	fun publishMessage(msg: String) {
		try {
			val message = MqttMessage()
			message.payload = msg.toByteArray()
			mqttAndroidClient?.publish(PUBLISH_TOPIC, message)
		} catch (e: MqttException) {
			e.printStackTrace()
		}
	}
	
	override fun onDestroy() {
		//服务退出时client断开连接
		mqttAndroidClient?.unregisterResources()
		mqttAndroidClient?.close()
		mqttAndroidClient?.disconnect()
		mqttAndroidClient?.setCallback(null)
		mqttAndroidClient = null
		super.onDestroy()
	}
	
	override fun onUnbind(intent: Intent?): Boolean {
		mqttAndroidClient?.unregisterResources()
		return super.onUnbind(intent)
	}
	
	
}