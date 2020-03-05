package com.example.rubbishcommunity.model.api.moments

import com.example.rubbishcommunity.model.api.LocationReq

data class GetMomentsRequestModel(
	val locationReq: LocationReq,
	val pageParam: PageParam?
)

data class PageParam(
	val pageNum:Int,
	val pageSize:Int,
	val limitKey:String?
)