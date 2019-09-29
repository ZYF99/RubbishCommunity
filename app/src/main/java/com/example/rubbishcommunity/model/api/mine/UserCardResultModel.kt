package com.example.rubbishcommunity.model.api.mine

import java.io.Serializable


class UserCardResultModel(
	val uin: String,
	val id: String,
	val name: String,
	val portrait: String,
	val city: String,
	val content: String,
	val age: Int
):Serializable {
	companion object {
		fun getNull(): UserCardResultModel {
			return UserCardResultModel(
				"",
				"",
				"未命名",
				"",
				"",
				"",
				0
			)
		}
	}
	
}


