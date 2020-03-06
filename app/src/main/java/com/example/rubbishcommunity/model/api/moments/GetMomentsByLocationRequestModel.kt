package com.example.rubbishcommunity.model.api.moments

import com.example.rubbishcommunity.model.api.LocationReq

data class GetMomentsRequestModel(
	val locationReq: LocationReq,
	val pageParam: PageParam?
)

