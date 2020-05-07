package com.example.rubbishcommunity.model.api.message

data class SendChatMessageRequestModel(
	val content: String,
	val messageType: Int = 0, //文本消息 0
	val receiver: Long
)