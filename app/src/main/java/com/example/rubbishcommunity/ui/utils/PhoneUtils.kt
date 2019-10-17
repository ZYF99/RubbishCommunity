package com.example.rubbishcommunity.ui.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.annotation.RequiresApi
import java.io.UnsupportedEncodingException
import java.util.*


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
	
	
	
	var id :String? =null
	
	val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
	if (!TextUtils.isEmpty(androidId) && "9774d56d682e549c" != androidId) {
		try {
			val uuid = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
			id = uuid.toString();
		} catch (e: UnsupportedEncodingException) {
			e.printStackTrace();
		}
	}
	
	if (TextUtils.isEmpty(id)) {
		id = getUUID()
	}
	return if(TextUtils.isEmpty(id)){
		UUID.randomUUID().toString()
	}else{
		id!!
	}

	
}

@SuppressLint("MissingPermission")
private fun getUUID() :String{
	var serial :String? = null
	
	val m_szDevIDShort = "35" +
	Build.BOARD.length % 10 + Build.BRAND.length % 10 +
			
			Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
			
			Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
			
			Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
			
			Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
			
			Build.TAGS.length % 10 + Build.TYPE.length % 10 +
			
			Build.USER.length % 10; //13 位
	
	try {
		serial = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			Build.getSerial()
		} else {
			Build.SERIAL
		}
		//API>=9 使用serial号
		return  UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
	} catch ( exception:Exception) {
		serial = "serial"; // 随便一个初始化
	}
	
	//使用硬件信息拼凑出来的15位号码
	return  UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
}

	
