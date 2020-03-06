package com.example.rubbishcommunity.model.api.moments

data class PageParam(
	val pageNum: Int,
	val pageSize: Int,
	val limitKey: String = ""
)