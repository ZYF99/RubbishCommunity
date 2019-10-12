package com.example.rubbishcommunity.ui.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import java.util.Locale


/**
 * 获取当前手机系统语言。
 *
 * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
 */
val systemLanguage: String
	get() = Locale.getDefault().language

/**
 * 获取当前系统上的语言列表(Locale列表)
 *
 * @return 语言列表
 */
val systemLanguageList: Array<Locale>
	get() = Locale.getAvailableLocales()

/**
 * 获取当前手机系统版本号
 *
 * @return 系统版本号
 */
val getSystemVersion: String
	get() = android.os.Build.VERSION.RELEASE


/**
 * [获取应用程序版本名称信息]
 * @param context
 * @return 当前应用的版本名称
 */
@Synchronized
fun getVersionName(context: Context): String? {
	try {
		val packageManager = context.packageManager
		val packageInfo = packageManager.getPackageInfo(
			context.packageName, 0
		)
		return packageInfo.versionName
	} catch (e: Exception) {
		e.printStackTrace()
	}
	
	return null
}

/**
 * 获取手机型号
 *
 * @return 手机型号
 */
val getSystemModel: String
	get() = android.os.Build.MODEL

/**
 * 获取手机厂商
 *
 * @return 手机厂商
 */
val getDeviceBrand: String
	get() = android.os.Build.BRAND

/**
 * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
 *
 * @return 手机IMEI
 */

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("HardwareIds", "MissingPermission")
fun getPhoneIMEI(context: Context): String {
	val tm = context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
	return tm.deviceId
}
	
