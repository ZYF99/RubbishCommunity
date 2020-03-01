package com.example.rubbishcommunity.model.api.guide

data class CompleteInfoRequestModel(
	val email:String,
	val avatar: String,
	val birthday: Long,
	val code: String,
	val gender: String,
	val idType:Int,
	val locationReq: LocationReq,
	val name: String

) {
	data class LocationReq(
		val city: String,
		val country: String,
		val district: String,
		val latitude: Double,
		val longitude: Double,
		val province: String,
		val street: String
	)
	
}