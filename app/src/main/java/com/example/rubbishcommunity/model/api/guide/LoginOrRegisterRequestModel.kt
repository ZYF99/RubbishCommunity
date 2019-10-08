package com.example.rubbishcommunity.model.api.guide

data class LoginOrRegisterRequestModel(
	val deviceInfo: DeviceInfo,
	val idType: Int,
	val password: String,
	val register: Boolean,
	val userName: String
) {
	
	data class DeviceInfo(
		val appVersion: String,
		val devName: String,
		val imei: String,
		val platform:String,
		val systemModel: String,
		val systemVersion: String
	)
	
	
}