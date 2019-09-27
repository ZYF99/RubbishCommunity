package com.example.rubbishcommunity

import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.example.rubbishcommunity.manager.base.apiModule
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import rx_activity_result2.RxActivityResult

var instance: MyApplication? = null


class MyApplication : MultiDexApplication(), KodeinAware {
	override val kodein = Kodein.lazy {
		import(apiModule)
	}
	companion object {
		lateinit var instance: MyApplication
		fun showToast(str: String) {
			Toast.makeText(instance, str, Toast.LENGTH_SHORT).show()
		}
	}
	
	override fun onCreate() {
		super.onCreate()
		SharedPreferencesUtils.getInstance(this, "local")
		RxActivityResult.register(this)
		instance = this
	}
	
}

