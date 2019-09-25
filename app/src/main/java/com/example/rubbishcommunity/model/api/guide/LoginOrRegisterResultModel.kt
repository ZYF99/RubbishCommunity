package com.example.rubbishcommunity.model.api.guide

data class LoginOrRegisterResultModel(
	val token: String,
	val usrProfile: UsrProfile,
	val usrSetting: UserSetting,
	val usrStatusFlag: UserStatusFlag,
	val lastLoginInfo: LastLoginInfo
) {
	class UsrProfile(
		val name: String,
		val avatar: String,
		val city: String,
		val age: Int,
		val birthday: String,
		val work: String,
		val gender: Int,
		val content: String,
		val level: Int,
		val aboutMe: String,
		val school: String,
		val company: String,
		val education: String,
		val profileCompletion: Double
	) {
		companion object {
			fun getNull(): UsrProfile {
				return UsrProfile(
					"",
					"",
					"",
					0,
					"",
					"",
					0,
					"",
					0,
					"",
					"",
					"",
					"",
					0.0
				)
			}
			
			
		}
		
	}
	
	data class UserSetting(
		val notification: Boolean,
		val voiceNotification: Boolean,
		val theme: String
	)
	
	data class UserStatusFlag(
		val emailVerifiedFlag: Boolean,
		val disableFlag: Boolean,
		val needMoreInfoFlag: Boolean
	)
	
	data class LastLoginInfo(
		val device: String,
		val lastLoginTime: String
	)
	
}