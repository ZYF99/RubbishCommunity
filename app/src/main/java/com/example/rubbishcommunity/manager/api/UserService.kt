package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.api.OpenIdModel
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.UinModel
import com.example.rubbishcommunity.model.api.guide.CompleteInfoRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.model.api.mine.UsrProfileResp
import com.example.rubbishcommunity.model.api.password.ResetPasswordRequestModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 14：12
 */
interface UserService {
	
	//登录或注册
	@POST("api/account/login")
	fun loginOrRegister(
		@Body loginOrRegisterRequestModel: LoginOrRegisterRequestModel
	): Single<ResultModel<LoginOrRegisterResultModel>>
	
	//邮箱验证
	@GET("api/common/tools/code/{email}/send")
	fun sendEmail(@Path("email") email: String): Single<ResultModel<String>>
	
	//完善用户信息
	@POST("api/account/profile/new")
	fun completeInfo(@Body completeInfoRequestModel: CompleteInfoRequestModel): Single<ResultModel<String>>
	
	//修改用户信息
	@PUT("api/account/profile/modify")
	fun editUserInfo(@Body modifyMap: HashMap<String, String>): Single<ResultModel<String>>
	
	//刷新用户详细信息
	@GET("api/account/profile/refresh")
	fun fetchUserProfile(@Query ("openId")openId:String? = null): Single<ResultModel<UsrProfileResp>>
	
	//获取用户卡片信息（头像 名字 签名等基本信息）
	@GET("api/account/info/{openId}/get")
	fun fetchUserCardByOpenId(@Path("openId") openId: String?): Single<ResultModel<UsrProfileResp>>
	
	//注销
	@POST("api/account/logout")
	fun logout(): Single<ResponseBody>
	
	//修改密码
	@PUT("api/account/password/modify")
	fun editPassword(@Body passwordReq: ResetPasswordRequestModel): Single<ResultModel<String>>
	
	//openId转uin
	@GET("api/common/tools/openId")
	fun openIdToUin(@Query("openId")openId: String?): Single<ResultModel<UinModel>>
	
	//uin转openId
	@GET("api/common/tools/openId")
	fun uinToOpenId(@Query("uin")uin: Long?): Single<ResultModel<OpenIdModel>>
	
}