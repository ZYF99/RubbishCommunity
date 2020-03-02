package com.example.rubbishcommunity.model.api.release.draft

data class GetDraftResultModel(
	private val dynamicId: Long = 0,
	val longitude: Double,
	val latitude: Double,
	val content: String?,
	val title: String?,
	val pictures: List<String>?,
	val topic: String?,
	val classify: Int?,
	val updateTime: Long?
)

