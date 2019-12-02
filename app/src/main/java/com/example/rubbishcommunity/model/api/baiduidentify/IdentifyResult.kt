package com.example.rubbishcommunity.model.api.baiduidentify

import com.google.gson.annotations.SerializedName

data class IdentifyResult(
	@SerializedName("log_id")
	val logId:Long,
	@SerializedName("result_num")
	val resultNum:Int,
	@SerializedName("result")
	val result:MutableList<Thing>
)

data class Thing(
	@SerializedName("score")
	val score:Double,
	@SerializedName("root")
	val root:String,
	@SerializedName("keyword")
	val keyword:String

)
