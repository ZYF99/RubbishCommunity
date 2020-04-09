package com.example.rubbishcommunity.model.api.moments


data class GetMomentsByUinRequestModel(
	val uin: Long?=null,
	val openId: String?=null,
	val pageParamRequest: PageParam?
)

