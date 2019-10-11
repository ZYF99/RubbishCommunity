package com.example.rubbishcommunity.model.api.mine

import java.io.Serializable


class UserCardResultModel(
	val uin: Int,
	val id: String,
	val name: String,
	val portrait: String,
	val city: String,
	val content: String,
	val age: Int
):Serializable {
	companion object {
		fun getDefault(): UserCardResultModel {
			return UserCardResultModel(
				0,
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


