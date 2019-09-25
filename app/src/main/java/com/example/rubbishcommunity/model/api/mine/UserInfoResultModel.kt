package com.example.rubbishcommunity.model.api.mine

data class UserInfoResultModel(
	val uin: String,
	val id: String,
	val name: String,
	val portrait: String,
	val city: String,
	val content: String,
	val age: Int
)
fun getNull(): UserInfoResultModel {
	return UserInfoResultModel(
		"",
		"",
		"",
		"",
		"",
		"",
		0
	)
	
}