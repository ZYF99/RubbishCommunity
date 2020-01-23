package com.example.rubbishcommunity.persistence


import android.annotation.SuppressLint
import com.baidu.location.BDLocation
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import com.example.rubbishcommunity.model.api.search.Category
import com.example.rubbishcommunity.utils.getAgeByBirth
import java.util.*

//存储用于验证的信息
fun saveVerifyInfo(
	userName: String,
	password: String,
	token: String,
	openId: String,
	emailVerifiedFlag: Boolean,
	needMoreInfoFlag: Boolean
) {
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


//存储分类信息
fun saveClassificationMap(map: List<Category>) {
	SharedPreferencesUtils.putListData(
		"classificationMap",
		map.toMutableList()
	)
}

//分类信息
@SuppressLint("UseSparseArrays")
fun getClassificationMap(): List<Category> {
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

//更新部分基本信息
fun updateSomeUserInfo(
	avatar: String,
	gender: String,
	name: String,
	birthday: Long,
	location: BDLocation
) {
	val oldUserInfo: UsrProfile = getLocalUserInfo()
	saveUserInfo(
		UsrProfile(
			name,
			avatar,
			location.country,
			location.province,
			location.city,
			location.street,
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

