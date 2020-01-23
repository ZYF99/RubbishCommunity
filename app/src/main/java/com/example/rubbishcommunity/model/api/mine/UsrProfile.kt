package com.example.rubbishcommunity.model.api.mine

class UsrProfile(
	val name: String,
	val avatar: String,
	val country: String,
	val province: String,
	val city: String,
	val street: String,
	val age: Int,
	val birthday: Long,
	val profession: String,
	val gender: String,
	val signature: String,
	val level: Int,
	val aboutMe: String,
	val school: String,
	val company: String,
	val degree: String,
	val language: String,
	val backgroundImage: String
) {
	companion object {
		fun getNull(): UsrProfile {
			return UsrProfile(
				"",
				"",
				"",
				"",
				"",
				"",
				0,
				0,
				"",
				"",
				"",
				0,
				"",
				"",
				"",
				"",
				"",
				""
			)
		}
		
		fun getDefault(): UsrProfile {
			return UsrProfile(
				"张天霸",
				"https://profile.csdnimg.cn/A/6/F/3_ethanco",
				"法国",
				"四川",
				"加州",
				"第五大道",
				66,
				23558400000,
				"代码清洁工",
				"男",
				"哈哈哈哈哈哈哈哈哈awsl",
				88,
				"关你屁事",
				"剑桥大学",
				"宇宙飞船",
				"梭梭树",
				"中文",
				"https://profile.csdnimg.cn/A/6/F/3_ethanco"
			)
		}
		
	}
}