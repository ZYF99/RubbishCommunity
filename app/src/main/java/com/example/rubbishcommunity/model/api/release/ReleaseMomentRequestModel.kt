package com.example.rubbishcommunity.model.api.release

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/11/2 16:10
 */


/**
 * 发布动态的请求数据
 * */

const val PUBLISH_TYPE_DEFAULT = 0 //默认
const val PUBLISH_TYPE_ORIGIN = 1 //原创
const val PUBLISH_TYPE_TRANS = 2 //转发

data class ReleaseMomentRequestModel(
	val latitude: Double?,
	val longitude: Double?,
	val momentId: Long,
	val publishType: Int = PUBLISH_TYPE_ORIGIN
)