package com.example.rubbishcommunity.model.api

data class LocationReq(
	val city: String = "成都",
	val country: String = "中国",
	val district: String = "高新区",
	val latitude: Long = 321.2233.toLong(),
	val longitude: Long = 321.2233.toLong(),
	val province: String = "四川",
	val street: String = "天才大道"
)