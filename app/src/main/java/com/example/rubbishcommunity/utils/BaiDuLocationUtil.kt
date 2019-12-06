package com.example.rubbishcommunity.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.provider.Settings
import com.example.rubbishcommunity.ui.base.BindingFragment
import io.reactivex.Observable
import rx_activity_result2.RxActivityResult
import timber.log.Timber


//private const val GPS_REQUEST_CODE = 20

fun BindingFragment<*, *>.checkLocationPermissionAndGetLocation(
	locationClient: LocationClient
): Observable<BDLocation?> {
	
	//真实获取定位
	fun getLocation() = Observable.create<BDLocation?> {
		locationClient.run {
			registerLocationListener(
				object : BDAbstractLocationListener() {
					@SuppressLint("BinaryOperationInTimber")
					override fun onReceiveLocation(bdLocation: BDLocation?) {
						if (bdLocation != null) {
							Timber.d(
								" 经度 ${bdLocation.longitude}\\n\" +\"纬度 ${bdLocation.latitude}\\n\"\n" +
										"\t\t\t\t +\"详细地址信息 ${bdLocation.addrStr}\\n\"\n" +
										"\t\t\t\t +\"国家 ${bdLocation.country}\\n\"\n" +
										"\t\t\t\t +\"省份 ${bdLocation.province}\\n\"\n" +
										"\t\t\t\t +\"城市 ${bdLocation.city}\\n\"\n" +
										"\t\t\t\t  +\"区县 ${bdLocation.district}\\n\"\n" +
										"\t\t\t\t  +\"街道 ${bdLocation.street}\\n"
							)
							it.onNext(bdLocation)
						} else
							Timber.d("The Location Received is null")
					}
				}
			)
			start()
		}
	}
	
	
	//确保打开定位服务后开始定位
	fun checkLocationServiceIsOpen(): Observable<Any> {
		return Observable.create {
			//检查定位服务是否开启
			val locationManager =
				this.activity!!.getSystemService(LOCATION_SERVICE) as LocationManager
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) it.onNext(1)
			else it.onError(NoLocationServiceError())
		}
	}
	
	
	//定位权限检查
	return checkLocationPermission()
		.flatMap {
			checkLocationServiceIsOpen()
		}.flatMap {
			getLocation()
		}
		.doOnError { error ->
			when (error) {
				is NullLocationError -> {
					error.printStackTrace()
				}
				is NoLocationServiceError -> {
					Observable.error<NoLocationServiceError>(error)
				}
				else -> {
					error.printStackTrace()
				}
			}
		}
}

fun BindingFragment<*, *>.showLocationServiceDialog(onSettingBackAction: () -> Unit) {
	AlertDialog.Builder(context).setTitle("需要定位").setMessage("需要开启定位")
		.setPositiveButton("确定") { _, _ ->
			val intent = Intent()
			intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
			RxActivityResult.on(this).startIntent(
				intent
			).doOnNext {
				onSettingBackAction()
			}.bindLife()
		}.setNegativeButton("取消") { _, _ ->
			activity!!.finish()
		}
		.setCancelable(false)
		.show()
}


//错误：返回的地址为空
class NullLocationError : Throwable()

//错误：未开启定位服务
class NoLocationServiceError : Throwable()
