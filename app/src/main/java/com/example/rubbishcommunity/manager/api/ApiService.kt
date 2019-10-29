package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.Dynamic
import com.example.rubbishcommunity.model.Message
import com.example.rubbishcommunity.model.Photography
import com.example.rubbishcommunity.model.Vote
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.CompleteInfoRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.model.api.mine.UserCardResultModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicRequestModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicResultModel
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import com.example.rubbishcommunity.ui.release.dynamic.GetQiNiuTokenRequestModel
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {
	
	//搜索分类
	@GET("api/common/tools/{searchKey}/search")
	fun searchClassification(@Path("searchKey") searchKey: String): Single<ResultModel<List<SearchKeyConclusion>>>
	
	
	//获取欢迎界面的写真
	@GET("api/welcome/photography/get")
	fun getPhotographyList():Single<ResultModel<List<Photography>>>
	
	//登陆或注册
	@POST("api/account/login")
	fun loginOrRegister(@Body loginOrRegisterRequestModel: LoginOrRegisterRequestModel): Single<ResultModel<LoginOrRegisterResultModel>>
	
	//邮箱验证
	@POST("api/common/tools/code/{email}/send")
	fun sendEmail(@Path("email") email: String): Single<ResultModel<String>>
	
	//完善用户信息
	@POST("api/account/profile/new")
	fun completeInfo(@Body completeInfoRequestModel: CompleteInfoRequestModel): Single<ResultModel<String>>
	
	//获取用户详细信息
	@GET("api/account/profile/{uid}/get")
	fun getUserInfo(@Path("uid") uid: String): Single<ResultModel<String>>
	
	//获取用户卡片信息（头像 名字 签名等基本信息）
	@GET("api/account/info/{uid}/get")
	fun getUserCard(@Path("uid") uid: String): Single<ResultModel<UserCardResultModel>>
	
	//注销
	@POST("api/account/logout")
	fun logout(): Single<ResultModel<String>>
	
	//获取七牛云上传图片凭证
	@POST("api/common/image/token")
	fun getQiNiuToken(@Body requestModel: GetQiNiuTokenRequestModel): Single<ResultModel<Map<String,String>>>
	

	//修改密码页*************************************************************************************
	
	//发送邮箱的验证码至服务器
	@POST("api/common/{verifyCode}/send")
	fun sendVerifyCode(@Path("verifyCode") verifyCode: String): Single<ResultModel<String>>
	//修改密码
	@PUT("api/account/password/modify")
	fun editPassword(@Path("password") password: String): Single<ResultModel<String>>
	//**********************************************************************************************
	

	
	@GET("api/dynamic/release")
	fun releaseDynamic(@Body releaseDynamicRequestModel: ReleaseDynamicRequestModel): Single<ResultModel<ReleaseDynamicResultModel>>
	
	@GET("hotWords?")
	fun getHotWordList(@Query("offset") offset: Int): Single<List<String>>
	
	@GET("dynamics?")
	fun getDynamicList(@Query("offset") offset: Int): Single<MutableList<Dynamic>>
	
	@GET("dynamic?")
	fun getDynamic(@Query("dynamicId") Id: String): Single<Dynamic>
	
	
	@GET("votes?")
	fun getVoteList(@Query("offset") offset: Int): Single<List<Vote>>
	
	@GET("vote?")
	fun getVote(@Query("voteId") Id: String): Single<Vote>
	
	
	
	//获取消息列表
	@GET("msgList")
	fun getMessageList(@Query("offset") offset: Int): Single<MutableList<Message>>
	
	//聊天部分***************************************************************************************
	@POST("chat")
	fun sendMsgToPeople(@Query("msg") msg: String): Single<String>

}