package com.example.rubbishcommunity.model.api

data class SimpleProfileResp(
	val openId: String,
	val name: String,
	val avatar: String,
	val signature: String,
	val country: String,
	val province: String,
	val city: String,
	val age: Int
)