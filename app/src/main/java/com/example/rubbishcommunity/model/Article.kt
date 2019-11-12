package com.example.rubbishcommunity.model


import java.io.Serializable

data class Article(
	val id: String,
	val iconUrl: String,
	val title: String,
	val msg: String,
	val time: String
) : Serializable
