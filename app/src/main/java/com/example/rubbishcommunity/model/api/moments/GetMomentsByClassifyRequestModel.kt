package com.example.rubbishcommunity.model.api.moments

import com.example.rubbishcommunity.model.api.LocationReq

data class GetMomentsByClassifyRequestModel(
	val classify: Int,
	val pageParam: PageParam?
)