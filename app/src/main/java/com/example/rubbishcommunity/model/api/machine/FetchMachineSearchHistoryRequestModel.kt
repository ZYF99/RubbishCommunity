package com.example.rubbishcommunity.model.api.machine

data class FetchMachineSearchHistoryRequestModel(
	val endTime: Long?,
	val macAddress: String?,
	val startTime: Long?
)