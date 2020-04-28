package com.example.rubbishcommunity.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue


class MainViewModel(application: Application) : BaseViewModel(application) {
	
	val appBarOffsetBias = MutableLiveData(0F)
	
	//当fragment的appbar滑动时调用了此方法 ，这里应该改变BottomNavigationView的位置
	fun onAppBarOffsetChanged(verticalOffset: Int, appbarHeight: Float) {
		appBarOffsetBias.value = verticalOffset.absoluteValue.toFloat() / appbarHeight
	}
	
/*	private val _parentKodein: Kodein by lazy { (application as MyApplication).kodein }
	
	override val kodein = Kodein {
		extend(_parentKodein, copy = Copy.All)
		import(mainModule)
	}
	
	val mqttClient by instance<MqttAndroidClient>()
	
	fun initMqtt(): Single<IMqttToken>{
		return mqttConnect(mqttClient)
			.doOnSuccess { MyApplication.showSuccess("MQTT连接成功") }
			.flatMap {
				mqttSubscribe(mqttClient)
			}.doOnSuccess {
				MyApplication
					.showSuccess("MQTT订阅成功")
				setDisconnectedBufferOptions(mqttClient)
			}
	}
	*/
	
}