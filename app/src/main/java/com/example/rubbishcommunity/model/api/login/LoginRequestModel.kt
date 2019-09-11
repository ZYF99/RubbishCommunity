package com.example.rubbishcommunity.model.api.login

data class LoginRequestModel(
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
		val osversion: String,
		val systemModel: String
	)
	
	
}