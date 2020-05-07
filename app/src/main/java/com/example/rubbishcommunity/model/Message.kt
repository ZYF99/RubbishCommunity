package com.example.rubbishcommunity.model


import com.example.rubbishcommunity.model.api.SimpleProfileResp
import java.io.Serializable

data class Message(
	val uid: String,
	val iconUrl: String,
	val title: String,
	val msg: String,
	val time: String,
	val user:SimpleProfileResp
) : Serializable
