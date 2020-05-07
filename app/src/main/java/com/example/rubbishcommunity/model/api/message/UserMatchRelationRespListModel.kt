package com.example.rubbishcommunity.model.api.message

import com.example.rubbishcommunity.model.api.SimpleProfileResp

data class UserMatchRelationRespListModel(
	val usrMatchRelationRespList: List<UserMatchRelationModel>
) {
	data class UserMatchRelationModel(
		val recipientProfile: SimpleProfileResp,
		val relationType: Int,
		val relationSource: Int,
		val refreshTime: Long
	)
}