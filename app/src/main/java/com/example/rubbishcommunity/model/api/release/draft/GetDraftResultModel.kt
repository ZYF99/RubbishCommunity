package com.example.rubbishcommunity.model.api.release.draft

data class GetDraftResultModel(
	val longitude: Double,
	val latitude: Double,
	val dynamic: String?,
	val pictures: List<String>,
	val topic: String?,
	val classify: Int,
	val updateTime: Double
	
)