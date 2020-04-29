package com.example.rubbishcommunity.ui.utils


import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.service.CHANNEL_ID_STRING
import com.example.rubbishcommunity.ui.home.MainActivity

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/11 18:10
 */
fun sendSimpleNotification(context: Context, title: String, contentString: String) {
	val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
	val contentIntent = PendingIntent.getActivity(
		context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
	)
	
	//适配8.0service
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		val mChannel =  NotificationChannel(CHANNEL_ID_STRING, "美社", NotificationManager.IMPORTANCE_HIGH)
		notificationManager?.createNotificationChannel(mChannel)
		notificationManager?.notify(
			Math.random().toInt(), NotificationCompat.Builder(
				MyApplication.instance,
				CHANNEL_ID_STRING
			).setContentTitle(title)
				.setContentText(contentString)
				.setWhen(System.currentTimeMillis())
				.setSmallIcon(R.drawable.icon_find_nor)
				.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.icon_find_nor))
				//.setFullScreenIntent(contentIntent,true)
				.setAutoCancel(true)
				.setTicker("悬浮通知")
				.setDefaults(DEFAULT_ALL)
		        .setPriority(NotificationManager.IMPORTANCE_HIGH)
				.build()
		)
	} else {
		notificationManager?.notify(
			Math.random().toInt(), NotificationCompat.Builder(MyApplication.instance)
				.setContentTitle(title)
				.setContentText(contentString)
				.setWhen(System.currentTimeMillis())
				.setSmallIcon(R.drawable.icon_find_nor)
				.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.icon_find_nor))
				//.setFullScreenIntent(contentIntent,true)
				.setAutoCancel(true)
				.setTicker("悬浮通知")
				.setDefaults(DEFAULT_ALL)
				.setPriority(NotificationManager.IMPORTANCE_HIGH)
				.build()
		)
	}
	
}


