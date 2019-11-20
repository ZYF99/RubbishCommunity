package com.example.rubbishcommunity.manager.base

import com.example.rubbishcommunity.BuildConfig
import com.example.rubbishcommunity.manager.api.*
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.concurrent.TimeUnit

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 8：12
 */
val apiModule = Kodein.Module {
	bind<ApiClient>() with singleton { provideApiClient() }
	
	bind<UserService>() with singleton { instance<ApiClient>().createService(UserService::class.java) }
	bind<ChatService>() with singleton { instance<ApiClient>().createService(ChatService::class.java) }
	bind<DynamicService>() with singleton { instance<ApiClient>().createService(DynamicService::class.java) }
	bind<ImageService>() with singleton { instance<ApiClient>().createService(ImageService::class.java) }
	bind<RubbishService>() with singleton { instance<ApiClient>().createService(RubbishService::class.java) }
	
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