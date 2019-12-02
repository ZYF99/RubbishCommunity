package com.example.rubbishcommunity.manager.base


import com.example.rubbishcommunity.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/11/20 14：12
 */
open class TianXingClient(
	private val retrofit: Retrofit,
	val okHttpClient: OkHttpClient
) {
	
	fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)
	
	class Builder(
		//val apiAuthorizations: MutableMap<String, Interceptor> = LinkedHashMap(),
		val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder(),
		private val adapterBuilder: Retrofit.Builder = Retrofit.Builder()
	) {
		fun build(url: String? = null): TianXingClient {
			val baseUrl = url ?: BuildConfig.TIANXING_URL

			adapterBuilder
				.baseUrl(baseUrl)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
			val client = okBuilder.addInterceptor {
					chain->
				val origin = chain.request()
				val request = origin
					.newBuilder()
					.header("Accept","application/json;charset=UTF-8")
					.header("Content-Type","application/x-www-form-urlencoded")
					.method(origin.method(),origin.body())
					.build()
				chain.proceed(request)
			}.build()
			
			val retrofit = adapterBuilder.client(client).build()
			return TianXingClient(retrofit, client)
		}
	}
	
}