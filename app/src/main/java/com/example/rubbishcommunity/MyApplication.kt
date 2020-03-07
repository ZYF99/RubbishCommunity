package com.example.rubbishcommunity

import android.content.Context
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.chibatching.kotpref.Kotpref
import com.example.rubbishcommunity.manager.base.apiModule
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.example.rubbishcommunity.utils.initHanLP
import es.dmoral.toasty.Toasty
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import rx_activity_result2.RxActivityResult
import timber.log.Timber


var instance: MyApplication? = null

class MyApplication : MultiDexApplication(), KodeinAware {
	override val kodein = Kodein.lazy {
		import(apiModule)
	}
	
	companion object {
		
		lateinit var instance: MyApplication
		
		fun showToast(str: String) {
			Toasty.normal(instance, str).show()
		}
		
		fun showWarning(str: String) {
			Toasty.warning(instance, str, Toast.LENGTH_SHORT, true).show()
		}
		
		fun showError(str: String) {
			Toasty.error(instance, str, Toast.LENGTH_SHORT, true).show()
		}
		
		fun showSuccess(str: String) {
			Toasty.success(instance, str, Toast.LENGTH_SHORT, true).show()
		}
	}
	
	override fun onCreate() {
		super.onCreate()
		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}
		SharedPreferencesUtils.getInstance(this, "local")
		Kotpref.init(this)
		RxActivityResult.register(this)
		instance = this
		initHanLP()
	}
}

/**
 * 初始化定位参数配置
 */
fun initLocationClient(context: Context?): LocationClient {
	//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
	val locationClient = LocationClient(context)
	val option = LocationClientOption()
	option.setIsNeedAddress(true)
	locationClient.locOption = option
	return locationClient
}
