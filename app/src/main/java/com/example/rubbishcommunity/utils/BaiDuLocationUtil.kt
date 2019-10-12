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
import com.luck.picture.lib.permissions.RxPermissions
import io.reactivex.Observable

/**
 * 初始化定位参数配置
 */
fun initLocationOption(context: Context):LocationClient{
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

/**
 * 实现定位回调
 */

class MyLocationListener : BDAbstractLocationListener() {
	override fun onReceiveLocation(location: BDLocation) {
		//此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
		//以下只列举部分获取经纬度相关（常用）的结果信息
		//更多结果信息获取说明，请参照类参考中BDLocation类中的说明
/*		//获取纬度信息
		val latitude = location.latitude
		//获取经度信息
		val longitude = location.longitude
		//获取定位精度，默认值为0.0f
		val radius = location.radius
		//获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
		val coorType = location.coorType
		//获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
		val errorCode = location.locType*/
		//地址
		val address = location.address
		print("AAAAAA ${address.toString()}")
		MyApplication.showToast(address.toString())
	}
}

//检查权限并获取定位
fun getLocationWithCheckPermission(activity:Activity,onReceiveLocationListener: BDAbstractLocationListener):Observable<Boolean>? {
	//定位权限检查
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		//定位权限检查
		if (
			(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
			(activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
		) {
			return RxPermissions(activity as Activity).request(
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.ACCESS_COARSE_LOCATION
			).doOnNext {
				if (!it) {
					//申请权限未通过
					MyApplication.showWarning("请打开定位权限以便于我们为您提供更好的服务～")
				} else {
					getLocation(onReceiveLocationListener)
				}
			}
		} else {
			getLocation(onReceiveLocationListener)
		}
	} else {
		getLocation(onReceiveLocationListener)
	}
	return null
}

//真实获取定位
fun getLocation(onReceiveLocationListener: BDAbstractLocationListener) {
	val locationClient = initLocationOption(MyApplication.instance)
	locationClient.registerLocationListener(object : BDAbstractLocationListener() {
		override fun onReceiveLocation(bdLocation: BDLocation?) {
			if (bdLocation != null) {
				onReceiveLocationListener.onReceiveLocation(bdLocation)
				locationClient.stop()
/*				location.postValue(
					"${bdLocation.locationDescribe}\n"
					*//*			"经度 ${bdLocation.longitude}\n" +"纬度 ${bdLocation.latitude}\n" +"详细地址信息 ${bdLocation.addrStr}\n" +"国家 ${bdLocation.country}\n" +"省份 ${bdLocation.province}\n" +"城市 ${bdLocation.city}\n" +"区县 ${bdLocation.district}\n" +"街道 ${bdLocation.street}\n"*//*
				)*/
			} else {
				MyApplication.showWarning("定位失败")
				locationClient.stop()
			}
		}
	})
	locationClient.start()
}
