package com.example.rubbishcommunity.model.api.machine

data class BindMachineRequestModel(
	val bindKey:String,
	val macAddress:String,
	val nikeName:String
)