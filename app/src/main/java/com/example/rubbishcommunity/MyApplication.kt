package com.example.rubbishcommunity

import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.example.rubbishcommunity.manager.base.apiModule
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.example.rubbishcommunity.utils.*
import es.dmoral.toasty.Toasty
import org.eclipse.paho.android.service.MqttAndroidClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import rx_activity_result2.RxActivityResult


var instance: MyApplication? = null


class MyApplication : MultiDexApplication(), KodeinAware {
	
	lateinit var mqttClient: MqttAndroidClient
	
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
		SharedPreferencesUtils.getInstance(this, "local")
		RxActivityResult.register(this)
		instance = this
		instance.mqttClient = initMqttClient(this)
		initHanLP()

	}
	
}

