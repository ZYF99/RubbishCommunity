package com.example.rubbishcommunity

import android.content.Context
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.example.rubbishcommunity.manager.base.apiModule
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.example.rubbishcommunity.utils.*
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
		RxActivityResult.register(this)
		instance = this
		initHanLP()
	}
	

	
}

/**
 * 初始化定位参数配置
 */
 fun initLocationClient(context: Context) :LocationClient{
	//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
	return LocationClient(context).apply {
		locOption = LocationClientOption().apply {
			
			//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
			locationMode = LocationClientOption.LocationMode.Device_Sensors
			
			//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			setCoorType("gcj02")
			
			//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
			setScanSpan(0)
			
			//可选，设置是否需要地址信息，默认不需要
			setIsNeedAddress(true)
			
			//可选，设置是否需要地址描述
			setIsNeedLocationDescribe(true)
			
			
			////可选，设置是否需要设备方向结果
			//setNeedDeviceDirect(false)
			
			// 设置定位场景，根据定位场景快速生成对应的定位参数  以出行场景为例
			//setLocationPurpose(LocationClientOption.BDLocationPurpose.Sport)
			
			//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
			 isLocationNotify = true
			
			
			//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
			//setIgnoreKillProcess(true)
			
			
			//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
			setIsNeedLocationDescribe(true)
			//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
			setIsNeedLocationPoiList(true)
			
			//可选，默认false，设置是否收集CRASH信息，默认收集
			SetIgnoreCacheException(true)
			
			//可选，默认false，设置是否开启Gps定位
			isOpenGps = true
			
			
			
			//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
			//setIsNeedAltitude(false)
			
			
			//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
			setOpenAutoNotifyMode()
			
			
			//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
			setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT)
		}
	}
}
