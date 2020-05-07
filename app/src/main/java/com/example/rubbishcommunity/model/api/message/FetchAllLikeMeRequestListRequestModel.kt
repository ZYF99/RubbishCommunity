package com.example.rubbishcommunity.model.api.message

data class FetchAllLikeMeRequestListRequestModel(
	val relationTypeList: List<Int> = listOf(20),
	val reverse: Boolean = true //查看谁想加我 true  查看我加的 false
)