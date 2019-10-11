package com.example.rubbishcommunity.persistence

import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel

//存储用于验证的信息
fun saveVerifyInfo(userName: String, password: String, token: String, openId: String,emailVerifiedFlag:Boolean,needMoreInfoFlag: Boolean) {
	
	
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
		"openId",
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

//存储用户基本信息
fun saveUserInfo(usrProfile: LoginOrRegisterResultModel.UsrProfile) {
	SharedPreferencesUtils.putData(
		"localUsrProfile",
		usrProfile
	)
}

//获取用户基本信息
fun getLocalUserInfo(): LoginOrRegisterResultModel.UsrProfile {
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
	return (SharedPreferencesUtils.getData("openId", "") as String)
}

//得到用户是否需要验证邮箱的flag
fun getLocalVerifiedEmail(): Boolean {
	return (SharedPreferencesUtils.getData("emailVerifiedFlag", false) as Boolean)
}

//得到用户是否需要更多信息的flag
fun getLocalNeedMoreInfo(): Boolean {
	return (SharedPreferencesUtils.getData("needMoreInfoFlag", false) as Boolean)
}

