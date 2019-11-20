package com.example.rubbishcommunity.manager.base


import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import android.util.Base64
import android.util.Base64.NO_WRAP

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 9：12
 */
class ProtoInterceptor : Interceptor {
	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain?): Response? {
		if (chain == null)
			return null
		val response = chain.proceed(chain.request())
		if (response.header("Content-Type") == "application/x-protobuf;charset=UTF-8") {
			return response.newBuilder().body(
				ResponseBody.create(
					response.body()?.contentType(),
					Base64.decode(response.body()?.bytes(), NO_WRAP)
				)
			).build()
		}
		return response
	}
}
