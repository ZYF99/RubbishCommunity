package com.example.rubbishcommunity.model.api.moments


data class GetMomentsByUinRequestModel(
	val uin: Long,
	val pageParamRequest: PageParam?
)

