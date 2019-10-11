package com.example.rubbishcommunity.model.api.guide

data class LoginOrRegisterResultModel(
	val openId:String,
	val token: String,
	val profileCompletion:String,
	val usrProfile: UsrProfile,
	val usrSetting: UserSetting,
	val usrStatusFlag: UserStatusFlag,
	val lastLoginInfo: LastLoginInfo
) {
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