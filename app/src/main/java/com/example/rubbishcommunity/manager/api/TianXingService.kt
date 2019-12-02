package com.example.rubbishcommunity.manager.api


import io.reactivex.Single
import retrofit2.http.Body

import retrofit2.http.POST

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 14：12
 */

//百度识别API
interface TianXingService {
	
	//传图获取名称及分类
	@POST("imglajifenlei/index?key=456f6de05f3ac11bb5791fee5df0d27e&img=base64")
	fun getThingName(@Body img: String): Single<String>
	
}