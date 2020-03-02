package com.example.rubbishcommunity.model.api.release

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/11/2 16:10
 */


/**
 * 发布动态的请求数据
 * */
data class ReleaseMomentRequestModel(
	val latitude: Double?,
	val longitude: Double?,
	val momentId: Long,
	val publishType: Int = 1
)