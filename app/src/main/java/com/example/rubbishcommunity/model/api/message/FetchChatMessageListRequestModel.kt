package com.example.rubbishcommunity.model.api.message

data class FetchChatMessageListRequestModel(
	val forward: Boolean,
	val sequenceId: Long,
	val size: Int,
	val timeLineId: Long
)