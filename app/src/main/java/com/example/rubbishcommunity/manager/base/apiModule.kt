package com.example.rubbishcommunity.manager.base

import com.example.rubbishcommunity.BuildConfig
import com.example.rubbishcommunity.manager.api.*
import com.google.protobuf.Api
import okhttp3.OkHttpClient
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
	
	bind<MachineService>() with singleton { instance<ApiClient>().createService(MachineService::class.java) }
	
	bind<RelationService>() with singleton { instance<ApiClient>().createService(RelationService::class.java) }
	
	//ip获取地址
	bind<IpClient>() with singleton { provideIpClient() }
	bind<IpService>() with singleton { instance<IpClient>().createService(IpService::class.java) }
	
	//聚合
	bind<JuheClient>() with singleton { provideJuheClient() }
	bind<JuheService>() with singleton { instance<JuheClient>().createService(JuheService::class.java) }
	
	//百度识别
	bind<BaiduIdentifyClient>() with singleton { provideBaiduIdentifyClient() }
	
	bind<BaiDuIdentifyService>() with singleton {
		instance<BaiduIdentifyClient>().createService(
			BaiDuIdentifyService::class.java
		)
	}
	
	
}


fun OkHttpClient.Builder.init() {
	val logInterceptor = HttpLoggingInterceptor()
	logInterceptor.level = HttpLoggingInterceptor.Level.BODY
	//addInterceptor(HeaderInterceptor())
	addInterceptor(NetErrorInterceptor())
		.addInterceptor(ProtoInterceptor())
		.apply {
			if (BuildConfig.DEBUG)
				addInterceptor(logInterceptor)
		}
		.readTimeout(10, TimeUnit.SECONDS)
}

fun provideApiClient(): ApiClient {
	val client = ApiClient.Builder()
	client.okBuilder.init()
	return client.build()
}

fun provideIpClient(): IpClient {
	val client = IpClient.Builder()
	client.okBuilder.init()
	return client.build()
}

fun provideJuheClient(): JuheClient {
	val client = JuheClient.Builder()
	client.okBuilder.init()
	return client.build()
}

fun provideBaiduIdentifyClient(): BaiduIdentifyClient {
	val client = BaiduIdentifyClient.Builder()
	client.okBuilder.init()
	return client.build()
}