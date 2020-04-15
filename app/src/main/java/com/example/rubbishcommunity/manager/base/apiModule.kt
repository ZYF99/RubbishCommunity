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
	//api
	bind<ApiClient>() with singleton { provideApiClient() }
	
	bind<UserService>() with singleton { instance<ApiClient>().createService(UserService::class.java) }
	
	bind<ChatService>() with singleton { instance<ApiClient>().createService(ChatService::class.java) }
	
	bind<MomentService>() with singleton { instance<ApiClient>().createService(MomentService::class.java) }
	
	bind<ImageService>() with singleton { instance<ApiClient>().createService(ImageService::class.java) }
	
	bind<RubbishService>() with singleton { instance<ApiClient>().createService(RubbishService::class.java) }
	
	bind<NewsService>() with singleton { instance<ApiClient>().createService(NewsService::class.java) }
	
	//ip获取地址
	bind<IpClient>() with singleton { provideIpClient() }
	bind<IpService>() with singleton { instance<IpClient>().createService(IpService::class.java) }
	
	//聚合
	bind<JuheClient>() with singleton { provideJuheClient() }
	bind<JuheService>() with singleton { instance<JuheClient>().createService(JuheService::class.java) }

	
	
	//百度识别
	bind<BaiduIdentifyClient>() with singleton { provideBaiDuIdentifyClient() }
	
	bind<BaiDuIdentifyService>() with singleton { instance<BaiduIdentifyClient>().createService(BaiDuIdentifyService::class.java) }
	
	//天行识别
	bind<TianXingClient>() with singleton { provideTianXingClient() }
	
	bind<TianXingService>() with singleton { instance<TianXingClient>().createService(TianXingService::class.java) }
	
	
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

fun provideIpClient(): IpClient {
	val client = IpClient.Builder()
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

fun provideBaiDuIdentifyClient(): BaiduIdentifyClient {
	val client = BaiduIdentifyClient.Builder()
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

fun provideTianXingClient(): TianXingClient {
	val client = TianXingClient.Builder()
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