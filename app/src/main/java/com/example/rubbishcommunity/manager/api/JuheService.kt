package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.BuildConfig
import com.example.rubbishcommunity.model.Photography
import com.example.rubbishcommunity.model.api.NewsResult
import io.reactivex.Single
import retrofit2.http.GET

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 14：12
 */
interface JuheService {
	
	//获取新闻
	@GET("index?type=top&key=${BuildConfig.JUHE_APPKEY}")
	fun getNews(): Single<NewsResult>
	
	//获取杂图
	@GET("index?type=top&key=${BuildConfig.JUHE_APPKEY}")
	fun getPhotography(): Single<MutableList<Photography>>
	
}