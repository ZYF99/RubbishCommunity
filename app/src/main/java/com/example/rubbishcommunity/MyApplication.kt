package com.example.rubbishcommunity

import android.app.Activity
import android.os.Build
import android.system.ErrnoException
import android.system.Os
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDexApplication
import com.example.rubbishcommunity.manager.base.apiModule
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.example.rubbishcommunity.utils.initHanLP
import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.corpus.io.IIOAdapter
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import es.dmoral.toasty.Toasty
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import rx_activity_result2.HolderActivity
import rx_activity_result2.RxActivityResult
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception


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
		SharedPreferencesUtils.getInstance(this, "local")
		RxActivityResult.register(this)
		instance = this
		initHanLP()
	}
	
	

	
	
}

