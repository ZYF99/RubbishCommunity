package com.example.rubbishcommunity.model.api

data class SimpleProfileResp(
	val openId: String,
	val name: String,
	val avatar: String,
	val signature: String,
	val country: String,
	val province: String,
	val city: String,
	val age: Int
){
	companion object{
		fun getDefault() = SimpleProfileResp(
			"fdmbhngsdbjsk",
			"dsfhg",
			"https://profile.csdnimg.cn/3/D/7/3_agsdfgdfhdf",
			"似懂非懂时间，返回;ewf",
			"中国",
			"四川",
			"成都",
			20
		)
	}
}