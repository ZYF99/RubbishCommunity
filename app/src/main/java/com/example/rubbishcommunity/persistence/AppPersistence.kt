package com.example.rubbishcommunity.persistence

import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.model.api.mine.UserInfoResultModel

//存储用于验证的信息
fun saveVerifyInfo(userName: String, password: String, token: String) {
	SharedPreferencesUtils.putData(
		"userName",
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
}

//存储用户基本信息
fun saveUserInfo(usrProfile: LoginOrRegisterResultModel.UsrProfile) {
	SharedPreferencesUtils.putData(
		"localUsrProfile",
		usrProfile
	)
}

//获取用户基本信息
fun getUserInfo(): LoginOrRegisterResultModel.UsrProfile? {
	return (SharedPreferencesUtils.getData(
		"localUsrProfile",
		LoginOrRegisterResultModel.UsrProfile.getNull()
	) as LoginOrRegisterResultModel.UsrProfile)
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
fun getLocalUserName(): String {
	return (SharedPreferencesUtils.getData("userName", "") as String)
}

//得到存储的密码
fun getLocalPassword(): String {
	return (SharedPreferencesUtils.getData("password", "") as String)
}

//得到存储的token
fun getLocalToken(): String {
	return (SharedPreferencesUtils.getData("token", "") as String)
}