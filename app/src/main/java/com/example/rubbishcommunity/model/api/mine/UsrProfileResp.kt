package com.example.rubbishcommunity.model.api.mine

import com.google.gson.annotations.SerializedName

data class UsrProfileResp(
	@SerializedName("usrProfileResp")
	val usrProfile: UsrProfile,
	val profileStatusResp: ProfileStatusResp
) {
	data class ProfileStatusResp(
		val completelyRegistrationFlag: String,
		val flagEmailVerify: String,
		val uinStatus: String,
		val uinType: String
	)
}

