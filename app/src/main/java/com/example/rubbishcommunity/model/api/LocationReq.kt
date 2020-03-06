package com.example.rubbishcommunity.model.api

data class LocationReq(
	val city: String = "成都",
	val country: String = "中国",
	val district: String = "高新区",
	val latitude: Double = 30.66667,
	val longitude: Double = 104.06667,
	val province: String = "四川",
	val street: String = "天才大道"
)