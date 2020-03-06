package com.example.rubbishcommunity.model


import java.io.Serializable

data class Message(
	val uid: String,
	val iconUrl: String,
	val title: String,
	val msg: String,
	val time: String
) : Serializable {
	
	override fun toString(): String {
		return "Message(uid='$uid', iconUrl='$iconUrl', title='$title', msg='$msg', time='$time')"
	}
}
