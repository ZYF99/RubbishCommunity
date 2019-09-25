package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.Dynamic
import com.example.rubbishcommunity.model.Vote
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.model.api.mine.UserInfoResultModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicRequestModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicResultModel
import com.example.rubbishcommunity.persistence.getLocalToken
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {
	
	/*登陆或注册*/
	@POST("api/account/login")
	fun loginOrRegister(@Body loginOrRegisterRequestModel: LoginOrRegisterRequestModel): Single<ResultModel<LoginOrRegisterResultModel>>
	
	
	/*
	* 未完成的接口
	*
	* */
	@POST("api/account/logout")
	fun logout(): Single<ResultModel<String>>
	
	
	@GET("api/account/info/{uid}/get")
	fun getUserInfo(@Path("uid") uid: String): Single<ResultModel<UserInfoResultModel>>
	
	
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
	
	
	@GET("api/dynamic/release")
	fun releaseDynamic(@Body releaseDynamicRequestModel: ReleaseDynamicRequestModel): Single<ResultModel<ReleaseDynamicResultModel>>
	
}