package com.example.rubbishcommunity.model.api.guide

import com.google.gson.annotations.SerializedName

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
		@SerializedName("imei")
		val iMei: String,
		val platform:String,
		val systemModel: String,
		val systemVersion: String
	)
	
	
}