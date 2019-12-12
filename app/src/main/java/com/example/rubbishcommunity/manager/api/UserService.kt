package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.CompleteInfoRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.model.api.mine.UserCardResultModel
import com.example.rubbishcommunity.model.api.mine.UsrProfileResp
import com.example.rubbishcommunity.model.api.password.ResetPasswordRequestModel
import io.reactivex.Single
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 14：12
 */
interface UserService {
	
	//登陆或注册
	@POST("api/account/login")
	fun loginOrRegister(@Body loginOrRegisterRequestModel: LoginOrRegisterRequestModel): Single<ResultModel<LoginOrRegisterResultModel>>
	
	//邮箱验证
	@GET("api/common/tools/code/{email}/send")
	fun sendEmail(@Path("email") email: String): Single<ResultModel<String>>
	
	//完善用户信息
	@POST("api/account/profile/new")
	fun completeInfo(@Body completeInfoRequestModel: CompleteInfoRequestModel): Single<ResultModel<String>>
	
	//修改用户信息
	//修改用户昵称
	@PUT("api/account/profile/modify")
	fun editUserInfo(@Body modifyMap: HashMap<String, String>): Single<ResultModel<String>>
	
	
	
	//刷新用户详细信息
	@GET("api/account/profile/refresh")
	fun getUserProfile(): Single<ResultModel<UsrProfileResp>>
	
	//获取用户卡片信息（头像 名字 签名等基本信息）
	@GET("api/account/info/{uid}/get")
	fun getUserCard(@Path("uid") uid: String): Single<ResultModel<UserCardResultModel>>
	
	//注销
	@POST("api/account/logout")
	fun logout(): Single<ResultModel<String>>
	
	//修改密码
	@PUT("api/account/password/modify")
	fun editPassword(@Body passwordReq: ResetPasswordRequestModel): Single<ResultModel<String>>
	

	
}