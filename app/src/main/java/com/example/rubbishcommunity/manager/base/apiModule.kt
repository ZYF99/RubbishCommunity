package com.example.rubbishcommunity.manager.base

import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.BuildConfig
import com.example.rubbishcommunity.manager.api.JuheService
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.concurrent.TimeUnit

val apiModule = Kodein.Module {
	bind<ApiClient>() with singleton { provideApiClient() }
	bind<ApiService>() with singleton { instance<ApiClient>().createService(ApiService::class.java) }
	
	bind<JuheClient>() with singleton { provideJuheClient() }
	bind<JuheService>() with singleton { instance<JuheClient>().createService(JuheService::class.java) }
	
}


fun provideApiClient(): ApiClient {
	val client = ApiClient.Builder()
	val logInterceptor = HttpLoggingInterceptor()
	logInterceptor.level = HttpLoggingInterceptor.Level.BODY
	
	client.okBuilder
    //.addInterceptor(HeaderInterceptor())
        .addInterceptor(NetErrorInterceptor())
		.addInterceptor(ProtoInterceptor())
		.apply {
			if (BuildConfig.DEBUG)
				addInterceptor(logInterceptor)
		}
		.readTimeout(10, TimeUnit.SECONDS)
	
	return client.build()
}


fun provideJuheClient(): JuheClient {
	val client = JuheClient.Builder()
	val logInterceptor = HttpLoggingInterceptor()
	logInterceptor.level = HttpLoggingInterceptor.Level.BODY
	
	client.okBuilder
		//.addInterceptor(HeaderInterceptor())
		.addInterceptor(NetErrorInterceptor())
		.apply {
			if (BuildConfig.DEBUG)
				addInterceptor(logInterceptor)
		}
		.readTimeout(10, TimeUnit.SECONDS)
	
	return client.build()
}