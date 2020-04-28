package com.example.rubbishcommunity.persistence

import android.annotation.SuppressLint
import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import com.example.rubbishcommunity.model.api.search.Category
import com.example.rubbishcommunity.utils.getAgeByBirth
import java.util.*

//存储用于验证的信息
fun saveVerifyInfo(
	uin: Long,
	userName: String,
	password: String,
	token: String,
	openId: String,
	emailVerifiedFlag: Boolean,
	needMoreInfoFlag: Boolean
) {
	SharedPreferencesUtils.putData(
		"uin",
		uin
	)
	SharedPreferencesUtils.putData(
		"email",
		userName
	)
	SharedPreferencesUtils.putData(
		"password",
		password
	)
	SharedPreferencesUtils.putData(
		"token",
		token
	)
	SharedPreferencesUtils.putData(
		"uid",
		openId
	)
	SharedPreferencesUtils.putData(
		"emailVerifiedFlag",
		emailVerifiedFlag
	)
	SharedPreferencesUtils.putData(
		"needMoreInfoFlag",
		needMoreInfoFlag
	)
}


//改变邮箱验证状态Flag
fun changeEmailVerifiedFlag(emailVerifiedFlag: Boolean) {
	SharedPreferencesUtils.putData(
		"emailVerifiedFlag",
		emailVerifiedFlag
	)
}

//改变需要更多信息的Flag
fun changeNeedMoreInfoFlag(needMoreInfoFlag: Boolean) {
	SharedPreferencesUtils.putData(
		"needMoreInfoFlag",
		needMoreInfoFlag
	)
}

//存储用户基本信息
fun saveUserInfo(usrProfile: UsrProfile) {
	SharedPreferencesUtils.putData(
		"localUsrProfile",
		usrProfile
	)
}

//得到用户基本信息
fun getUserSimpleProfile(): SimpleProfileResp {
	return SimpleProfileResp(
		getLocalOpenId(),
		getLocalUserInfo().name,
		getLocalUserInfo().avatar,
		getLocalUserInfo().signature,
		getLocalUserInfo().country,
		getLocalUserInfo().province,
		getLocalUserInfo().city,
		getLocalUserInfo().age
	)
}

//存储分类信息
fun saveClassificationMap(map: List<Category>) {
	SharedPreferencesUtils.putListData(
		"classificationMap",
		map.toMutableList()
	)
}

//分类信息
@SuppressLint("UseSparseArrays")
fun getClassificationList(): List<Category> {
	return SharedPreferencesUtils.getListData(
		"classificationMap",
		Category::class.java
	)
}


//获取用户基本信息
fun getLocalUserInfo(): UsrProfile {
	return (SharedPreferencesUtils.getData(
		"localUsrProfile",
		UsrProfile.getNull()
	) as UsrProfile)
}

//获取用户uin
fun getLocalUin(): Long {
	return (SharedPreferencesUtils.getData(
		"uin",
		0.toLong()
	) as Long)
}

//更新部分基本信息
fun updateSomeUserInfo(
	avatar: String,
	gender: String,
	name: String,
	birthday: Long,
	city: String
	//location: BDLocation
) {
	val oldUserInfo: UsrProfile = getLocalUserInfo()
	saveUserInfo(
		UsrProfile(
			name,
			avatar,
			"默认国家",
			"默认省份",
			city,
			"默认街道",
			getAgeByBirth(Date(birthday)),
			birthday,
			oldUserInfo.profession,
			gender,
			oldUserInfo.signature,
			oldUserInfo.level,
			oldUserInfo.aboutMe,
			oldUserInfo.school,
			oldUserInfo.company,
			oldUserInfo.degree,
			oldUserInfo.language,
			oldUserInfo.backgroundImage
		)
	)
	
}


//存储登陆状态
fun saveLoginState(boolean: Boolean) {
	SharedPreferencesUtils.putData(
		"isLogin",
		boolean
	)
}

//获取登陆状态
fun getLoginState(): Boolean {
	return (SharedPreferencesUtils.getData(
		"isLogin", false
	) as Boolean)
}

/**
 * 敏感数据
 **/
fun getLocalEmail(): String {
	return (SharedPreferencesUtils.getData("email", "") as String)
}


//得到存储的密码
fun getLocalPassword(): String {
	return (SharedPreferencesUtils.getData("password", "") as String)
}

//得到存储的token
fun getLocalToken(): String {
	return (SharedPreferencesUtils.getData("token", "") as String)
}

//得到用户的openId
fun getLocalOpenId(): String {
	return (SharedPreferencesUtils.getData("uid", "") as String)
}

//得到用户是否需要验证邮箱的flag
fun getLocalVerifiedEmail(): Boolean {
	return (SharedPreferencesUtils.getData("emailVerifiedFlag", false) as Boolean)
}

//得到用户是否需要更多信息的flag
fun getLocalNeedMoreInfo(): Boolean {
	return (SharedPreferencesUtils.getData("needMoreInfoFlag", false) as Boolean)
}

//存储维持心跳所需的linkKey
fun saveLinkKey(linkKey: String = UUID.randomUUID().toString().substring(0,5)) {
	SharedPreferencesUtils.putData(
		"linkKey",
		linkKey
	)
}

//得到维持心跳所需的linkKey
fun getLinkKey() =
	SharedPreferencesUtils.getData(
		"linkKey",
		"default"
	) as String




/*智能硬件*/

//存储用户搜索机器历史的开始时间
fun saveMachineSearchHistoryStartTime(startTime: Long) {
	SharedPreferencesUtils.putData(
		"startTime",
		startTime
	)
}

//存储用户搜索机器历史的开始时间
fun getMachineSearchHistoryStartTime() =
	SharedPreferencesUtils.getData(
		"startTime",
		1.toLong()
	) as Long


//存储用户搜索机器历史的开始时间
fun saveMachineSearchHistoryEndTime(endTime: Long) {
	SharedPreferencesUtils.putData(
		"endTime",
		endTime
	)
}

//存储用户搜索机器历史的开始时间
fun getMachineSearchHistoryEndTime() =
	SharedPreferencesUtils.getData(
		"endTime",
		Date().time
	) as Long
