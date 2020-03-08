package com.example.rubbishcommunity.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.rubbishcommunity.ui.base.BindingFragment
import io.reactivex.Observable
import rx_activity_result2.RxActivityResult
import timber.log.Timber


//private const val GPS_REQUEST_CODE = 20

@RequiresApi(Build.VERSION_CODES.Q)
fun BindingFragment<*, *>.checkLocationPermissionAndGetLocation(
	locationClient: LocationClient
): Observable<BDLocation?> {
	
	//真实获取定位
	fun getLocation() = Observable.create<BDLocation?> {
		locationClient.registerLocationListener(object : BDAbstractLocationListener() {
			override fun onReceiveLocation(bdLocation: BDLocation) {
				Timber.d("$bdLocation")
				it.onNext(bdLocation)
			}
		})
		locationClient.start()
	}
	
	@SuppressLint("MissingPermission")
	fun getLocationByAndroid(): Observable<Location> {
		return Observable.create<Location> { emitter ->
			val myLocationManager =
				context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
			val a = myLocationManager.getProvider(LocationManager.GPS_PROVIDER)
			if (ActivityCompat.checkSelfPermission(
					context!!,
					Manifest.permission.ACCESS_FINE_LOCATION
				) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
					context!!,
					Manifest.permission.ACCESS_COARSE_LOCATION
				) == PackageManager.PERMISSION_GRANTED
			)
				myLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, object :
					LocationListener {
					override fun onLocationChanged(location: Location?) {
						Timber.d("$location")
						if (location != null)
							emitter.onNext(location)
					}
					
					override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
					
					}
					
					override fun onProviderEnabled(provider: String?) {
					
					}
					
					override fun onProviderDisabled(provider: String?) {
					
					}
				}, null)
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
			//getLocationByAndroid()
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
