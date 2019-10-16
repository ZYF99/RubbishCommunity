package com.example.rubbishcommunity.model

data class ChatMessage(
	val openId:String,
	val iconUrl:String,
	val content:String,
	val time:Long
) {
}