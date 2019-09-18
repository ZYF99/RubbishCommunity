package com.example.rubbishcommunity.persistence

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

fun saveLoginState(boolean: Boolean) {
	SharedPreferencesUtils.putData(
		"isLogin",
		boolean
	)
}

fun getLoginState(): Boolean {
	return (SharedPreferencesUtils.getData(
		"isLogin", false
	) as Boolean)
}


fun getLocalUserName(): String {
	return (SharedPreferencesUtils.getData("userName", "") as String)
	
}

fun getLocalPassword(): String {
	return (SharedPreferencesUtils.getData("password", "") as String)
	
}

fun getLocalToken(): String {
	return (SharedPreferencesUtils.getData("token", "") as String)
	
}