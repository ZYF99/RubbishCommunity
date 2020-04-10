package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.BuildConfig
import com.example.rubbishcommunity.model.Photography
import com.example.rubbishcommunity.model.api.JuheNewsResult
import io.reactivex.Single
import retrofit2.http.GET

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 14：12
 */
interface JuheService {
	
	//获取资讯
	@GET("index?type=guoji&key=${BuildConfig.JUHE_APPKEY}")
	fun getNews(): Single<JuheNewsResult>
	
	//获取杂图
	@GET("index?type=guoji&key=${BuildConfig.JUHE_APPKEY}")
	fun getPhotography(): Single<MutableList<Photography>>
	
}