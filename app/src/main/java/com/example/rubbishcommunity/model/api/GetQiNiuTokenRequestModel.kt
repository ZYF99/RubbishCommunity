package com.example.rubbishcommunity.model.api

data class GetQiNiuTokenRequestModel(
	val bucketName: String,
	val fileKeys: List<String>
)