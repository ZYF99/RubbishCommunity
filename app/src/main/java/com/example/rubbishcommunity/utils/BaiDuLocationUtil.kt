package com.example.rubbishcommunity.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.initLocationClient
import com.example.rubbishcommunity.ui.base.BindingActivity
import com.example.rubbishcommunity.ui.base.rxRequestPermission


fun BindingActivity<*, *>.checkLocationPermissionAndGetLocation(
	locationClient: LocationClient,
	onReceiveLocation: (BDLocation) -> Unit
) {
	//定位权限检查
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		//定位权限检查
		if (
			(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
			(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
		) {
			rxRequestPermission(
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.ACCESS_COARSE_LOCATION
			).doOnNext {
				getLocation(locationClient, onReceiveLocation)
			}.bindLife()
			
		} else {
			//MyApplication.showSuccess("得到权限！")
			getLocation(locationClient, onReceiveLocation)
		}
	} else {
		//MyApplication.showSuccess("版本小于Android M")
		getLocation(locationClient, onReceiveLocation)
	}
}


//真实获取定位
fun getLocation(
	locationClient: LocationClient,
	onReceiveLocation: (BDLocation) -> Unit
) {
	locationClient.registerLocationListener(
		BaiduLocationListener(locationClient,onReceiveLocation)
	)
	locationClient.start()
}


class NoLocationException : Throwable()


class BaiduLocationListener(
	private val locationClient:LocationClient,
	val onLocationReceived: (BDLocation) -> Unit
) : BDAbstractLocationListener() {
	override fun onReceiveLocation(bdLocation: BDLocation) {
		locationClient.stop()
		if (bdLocation.addrStr != null) {
			onLocationReceived(bdLocation)
			Log.d("!!!!!!",
				" 经度 ${bdLocation.longitude}\\n\" +\"纬度 ${bdLocation.latitude}\\n\"\n" +
						"\t\t\t\t +\"详细地址信息 ${bdLocation.addrStr}\\n\"\n" +
						"\t\t\t\t +\"国家 ${bdLocation.country}\\n\"\n" +
						"\t\t\t\t +\"省份 ${bdLocation.province}\\n\"\n" +
						"\t\t\t\t +\"城市 ${bdLocation.city}\\n\"\n" +
						"\t\t\t\t  +\"区县 ${bdLocation.district}\\n\"\n" +
						"\t\t\t\t  +\"街道 ${bdLocation.street}\\n"
			)
		} else {
			locationClient.start()
		}
	}
}