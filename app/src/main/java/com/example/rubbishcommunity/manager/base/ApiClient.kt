package com.example.rubbishcommunity.manager.base

import android.annotation.SuppressLint
import com.example.rubbishcommunity.BuildConfig
import com.example.rubbishcommunity.persistence.getLocalToken
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.protobuf.ProtoConverterFactory
import okhttp3.Interceptor
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.reflect.KClass

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/7/20 15：12
 */


class ApiClient private constructor(val retrofit: Retrofit, val okHttpClient: OkHttpClient) {
	
	fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)
	
	fun <S : Any> createService(serviceClass: KClass<S>): S = createService(serviceClass.java)
	
	
	class Builder(
		val apiAuthorizations: MutableMap<String, Interceptor> = LinkedHashMap(),
		val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder(),
		val adapterBuilder: Retrofit.Builder = Retrofit.Builder()
	) {
		
		private val allowAllSSLSocketFactory: Pair<SSLSocketFactory, X509TrustManager>?
			get() {
				return try {
					val sslContext = SSLContext.getInstance("TLS")
					val trustManager = trustManagerAllowAllCerts
					sslContext.init(
						null,
						arrayOf<TrustManager>(trustManager),
						SecureRandom()
					)
					
					sslContext.socketFactory to trustManager
				} catch (e: Exception) {
					Timber.e(e, "allowAllSSLSocketFactory has error")
					null
				}
			}
		
		/**
		 * すべての証明書を受け付ける信頼マネージャを作成
		 * @return 信頼マネージャ
		 */
		private val trustManagerAllowAllCerts: X509TrustManager
			get() = object : X509TrustManager {
				override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
				
				@SuppressLint("TrustAllX509TrustManager")
				@Throws(CertificateException::class)
				override fun checkClientTrusted(
					chain: Array<X509Certificate>, authType: String
				) {
				}
				
				@SuppressLint("TrustAllX509TrustManager")
				@Throws(CertificateException::class)
				override fun checkServerTrusted(
					chain: Array<X509Certificate>, authType: String
				) {
				}
			}
		
		fun setAllowAllCerTificates(): Builder {
			allowAllSSLSocketFactory?.apply {
				okBuilder.sslSocketFactory(first, second)
				okBuilder.hostnameVerifier(HostnameVerifier { _, _ -> true })
			}
			
			return this
		}
		
		fun build(baseUrl: String = BuildConfig.BASE_URL): ApiClient {
			
			adapterBuilder
				.baseUrl(baseUrl)
				.addConverterFactory(ProtoConverterFactory.create())//一定要在gsonconvert的前面
				//.addConverterFactory(WireConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
			
			
			val client = okBuilder.addInterceptor { chain ->
				val origin = chain.request()
				val request = origin
					.newBuilder()
					.header("Accept", "application/json;charset=UTF-8")
					.header("X-Token", getLocalToken())
					.header("Content-Type", "application/x-www-form-urlencoded")
					.method(origin.method(), origin.body())
					.build()
				chain.proceed(request)
			}.build()
			
			if (BuildConfig.ALLOW_ALL_CERTIFICATES)
				setAllowAllCerTificates()
			
			val retrofit = adapterBuilder.client(client).build()
			return ApiClient(retrofit, client)
		}
	}
	
	companion object {
		val defaultClient: ApiClient
			get() = Builder().build()
	}
}

