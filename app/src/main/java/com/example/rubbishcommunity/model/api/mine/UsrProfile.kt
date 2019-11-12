package com.example.rubbishcommunity.model.api.mine

class UsrProfile(
	val name: String,
	val avatar: String,
	val country: String,
	val province: String,
	val city: String,
	val street: String,
	val age: Int,
	val birthday: String,
	val profession: String,
	val gender: String,
	val signature: String,
	val level: Int,
	val aboutMe: String,
	val school: String,
	val company: String,
	val degree: String,
	val language: String,
	val backgroundImage: String
) {
	companion object {
		fun getNull(): UsrProfile {
			return UsrProfile(
				"",
				"",
				"",
				"",
				"",
				"",
				0,
				"",
				"",
				"",
				"",
				0,
				"",
				"",
				"",
				"",
				"",
				""
			)
		}
	}
}