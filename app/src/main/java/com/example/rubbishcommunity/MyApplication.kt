package com.example.rubbishcommunity

import android.os.Build
import android.system.ErrnoException
import android.system.Os
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.multidex.MultiDexApplication
import com.example.rubbishcommunity.manager.base.apiModule
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.corpus.io.IIOAdapter
import es.dmoral.toasty.Toasty
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import rx_activity_result2.RxActivityResult
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


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
	
	
	//初始化汉语言处理包
	private fun initHanLP() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			try {
				Os.setenv("HANLP_ROOT", "", true)
			} catch (e: ErrnoException) {
				throw RuntimeException(e)
			}
		}
		val assetManager = assets
		HanLP.Config.IOAdapter = object : IIOAdapter {
			@Throws(IOException::class)
			override fun open(path: String): InputStream {
				return assetManager.open(path)
			}
			
			@Throws(IOException::class)
			override fun create(path: String): OutputStream {
				throw IllegalAccessError("不支持写入$path！请在编译前将需要的数据放入app/src/main/assets/data")
			}
		}
	}
	
	
}

