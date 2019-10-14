package com.example.rubbishcommunity.ui.utils


import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.ui.home.MainActivity


fun sendSimpleNotification(context: Context, title: String, contentString: String) {
	val mNotifyMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
	val contentIntent = PendingIntent.getActivity(
		context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
	)
	val notificationBuilder = Notification.Builder(context)
		.setSmallIcon(R.drawable.bg)
		.setContentTitle(title)
		.setContentText(contentString)
		.setAutoCancel(true)
		.setFullScreenIntent(contentIntent,true)
	notificationBuilder.setAutoCancel(true)
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
		val notification = notificationBuilder.build()// getNotification()
		mNotifyMgr!!.notify(2, notification)
	}
}
