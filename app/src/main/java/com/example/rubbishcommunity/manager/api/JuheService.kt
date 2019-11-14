package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.api.NewsResult
import io.reactivex.Single
import retrofit2.http.GET

interface JuheService {
	
	//获取新闻
	@GET("index?type=top&key=3dc86b09a2ee2477a5baa80ee70fcdf5")
	fun getNews(): Single<NewsResult>
	
}