package com.example.rubbishcommunity.model.api.login

data class LoginResultModel(
	val token: String,
	val usrProfile: UsrProfile,
	val usrSetting: UserSetting,
	val usrStatusFlag: UserStatusFlag,
	val lastLoginInfo: LastLoginInfo
) {
	data class UsrProfile(
		val name: String,
		val avatar: String,
		val city: String,
		val age: Int,
		val birthday: String,
		val work: String,
		val gender: Int,
		val level: Int,
		val aboutMe: String,
		val school: String,
		val company: String,
		val education: String,
		val profileCompletion: Double
	)
	
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