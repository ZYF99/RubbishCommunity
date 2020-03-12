package com.example.rubbishcommunity.model.api.moments

data class GetMomentsByClassifyRequestModel(
	val classify: Int,
	val pageParam: PageParam?
)