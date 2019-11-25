package com.example.rubbishcommunity.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.luck.picture.lib.permissions.RxPermissions
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


class BaiDuLocationUtil

/**
 * 初始化定位参数配置
 */
fun initLocationOption(context: Context): LocationClient {
	//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
	val locationClient = LocationClient(context)
	
	//可选，是否需要地址信息，默认为不需要，即参数为false
	//如果开发者需要获得当前点的地址信息，此处必须为true
	
	//mLocationClient为第二步初始化过的LocationClient对象
	//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
	//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
	
	//声明LocationClient类实例并配置定位参数
	val locationOption = LocationClientOption()
/*	val myLocationListener = MyLocationListener()
	//注册监听函数
	locationClient.registerLocationListener(myLocationListener)*/
	//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
	locationOption.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
	//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
	locationOption.setCoorType("gcj02")
	//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
	locationOption.setScanSpan(0)
	//可选，设置是否需要地址信息，默认不需要
	locationOption.setIsNeedAddress(true)
	//可选，设置是否需要地址描述
	locationOption.setIsNeedLocationDescribe(true)
	//可选，设置是否需要设备方向结果
	locationOption.setNeedDeviceDirect(false)
	// 设置定位场景，根据定位场景快速生成对应的定位参数  以出行场景为例
	locationOption.setLocationPurpose(LocationClientOption.BDLocationPurpose.Sport);
	//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
	locationOption.isLocationNotify = false
	//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
	locationOption.setIgnoreKillProcess(true)
	//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
	locationOption.setIsNeedLocationDescribe(true)
	//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
	locationOption.setIsNeedLocationPoiList(true)
	//可选，默认false，设置是否收集CRASH信息，默认收集
	locationOption.SetIgnoreCacheException(false)
	//可选，默认false，设置是否开启Gps定位
	locationOption.isOpenGps = true
	//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
	locationOption.setIsNeedAltitude(false)
	//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
	locationOption.setOpenAutoNotifyMode()
	//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
	locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT)
	//将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
	locationClient.locOption = locationOption
	
	return locationClient
}


//检查权限并获取定位
fun checkLocationPermissionAndGetLocation(
	activity: Activity,
	locationClient: LocationClient,
	onReceiveLocation: (BDLocation) -> Unit
): Observable<Boolean>? {
	//定位权限检查
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		//定位权限检查
		if (
			(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
			(activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
		) {
			return RxPermissions(activity).request(
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.ACCESS_COARSE_LOCATION
			).doOnNext {
				if (!it) {
					//申请权限未通过
					throw NoLocationException()
				} else {
					getLocation(locationClient, onReceiveLocation)
				}
			}.doOnError {
				if (it is NoLocationException)
					sendError(ErrorType.NO_LOCATION, "请打开定位权限以便于我们精准扶贫！！～")
			}.retryWhen {
				it.flatMap {
					Observable.timer(3,TimeUnit.SECONDS)
				}
			}
		} else {
			getLocation(locationClient, onReceiveLocation)
		}
	} else {
		getLocation(locationClient, onReceiveLocation)
	}
	return null
}

//真实获取定位
fun getLocation(
	locationClient: LocationClient,
	onReceiveLocation: (BDLocation) -> Unit
) {
	locationClient.registerLocationListener(object : BDAbstractLocationListener() {
		override fun onReceiveLocation(bdLocation: BDLocation?) {
			if (bdLocation != null) {
				onReceiveLocation(bdLocation)
				locationClient.stop()
				/*
				 经度 ${bdLocation.longitude}\n" +"纬度 ${bdLocation.latitude}\n"
				 +"详细地址信息 ${bdLocation.addrStr}\n"
				 +"国家 ${bdLocation.country}\n"
				 +"省份 ${bdLocation.province}\n"
				 +"城市 ${bdLocation.city}\n"
				  +"区县 ${bdLocation.district}\n"
				  +"街道 ${bdLocation.street}\n
				  */
			} else {
				MyApplication.showWarning("定位失败")
				locationClient.stop()
			}
		}
	})
	locationClient.start()
}


class NoLocationException : Throwable()