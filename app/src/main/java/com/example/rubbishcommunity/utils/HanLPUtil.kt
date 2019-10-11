package com.example.rubbishcommunity.utils

import android.os.Build
import android.system.ErrnoException
import android.system.Os
import com.example.rubbishcommunity.MyApplication
import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.corpus.io.IIOAdapter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

//初始化汉语言处理包
fun initHanLP() {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		try {
			Os.setenv("HANLP_ROOT", "", true)
		}catch (e:HanLPInputError){
			throw e
		} catch (e: ErrnoException) {
			e.printStackTrace()
		}
	}
	val assetManager = MyApplication.instance.assets
	HanLP.Config.IOAdapter = object : IIOAdapter {
		@Throws(IOException::class)
		override fun open(path: String): InputStream {
			return assetManager.open(path)
		}
		
		@Throws(IOException::class)
		override fun create(path: String): OutputStream {
			//throw IllegalAccessError("不支持写入$path！请在编译前将需要的数据放入app/src/main/assets/data")
			throw HanLPInputError("很遗憾，没有找到结果，去社区问问大家吧～")
		}
	}
}

class HanLPInputError(val str:String) :Throwable()