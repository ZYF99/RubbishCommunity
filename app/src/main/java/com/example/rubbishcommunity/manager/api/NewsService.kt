package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.news.NewsResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 14：12
 */
interface NewsService {
	
	//获取资讯
	@GET("api/news")
	fun fetchNews(
		@Query("pageSize")pageSize:Int = 10,
		@Query("syncKey")syncKey:Long?
	): Single<ResultModel<NewsResult>>
	
}