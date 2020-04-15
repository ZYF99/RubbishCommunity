package com.example.rubbishcommunity.manager.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/11/20 14：12
 */
interface IpService {
	
	//获取消息列表
	@GET("cityjson")
	fun getAddress(@Query("ie")ie:String = "utf-8"): Single<ResponseBody>
	
}