package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.Dynamic
import com.example.rubbishcommunity.model.Vote
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.EmailRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.model.api.mine.UserCardResultModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicRequestModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicResultModel
import com.example.rubbishcommunity.ui.release.dynamic.GetQiNiuTokenRequestModel
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {
	
	//登陆或注册
	@POST("api/account/login")
	fun loginOrRegister(@Body loginOrRegisterRequestModel: LoginOrRegisterRequestModel): Single<ResultModel<LoginOrRegisterResultModel>>
	
	//邮箱验证
	@POST("api/common/tools/code/send")
	fun sendEmail(@Body emailRequestModel: EmailRequestModel): Single<ResultModel<String>>
	
	//获取用户详细信息
	@GET("api/account/info/{uid}/get")
	fun getUserInfo(@Path("uid") uid: String): Single<ResultModel<LoginOrRegisterResultModel.UsrProfile>>
	
	//获取用户卡片信息（头像 名字 签名等基本信息）
	@GET("api/account/info/{uid}/get")
	fun getUserCard(@Path("uid") uid: String): Single<ResultModel<UserCardResultModel>>

	/*
	* 未完成的接口
	*
	* */
	
	//注销
	@POST("api/account/logout")
	fun logout(): Single<ResultModel<String>>
	
	//获取七牛云上传图片凭证
	@POST("api/common/image/token")
	fun getQiNiuToken(@Body requestModel: GetQiNiuTokenRequestModel): Single<ResultModel<Map<String,String>>>
	
	
	@GET("api/dynamic/release")
	fun releaseDynamic(@Body releaseDynamicRequestModel: ReleaseDynamicRequestModel): Single<ResultModel<ReleaseDynamicResultModel>>
	
	
	
	@GET("api/account/profile/{uid}/get")
	fun getUserInfoDetail(@Path("uid") uid: String): Single<String>
	
	
	@GET("hotWords?")
	fun getHotWordList(@Query("offset") offset: Int): Single<List<String>>
	
	@GET("dynamics?")
	fun getDynamicList(@Query("offset") offset: Int): Single<List<Dynamic>>
	
	@GET("dynamic?")
	fun getDynamic(@Query("dynamicId") Id: String): Single<Dynamic>
	
	
	@GET("votes?")
	fun getVoteList(@Query("offset") offset: Int): Single<List<Vote>>
	
	@GET("vote?")
	fun getVote(@Query("voteId") Id: String): Single<Vote>
	
	

}