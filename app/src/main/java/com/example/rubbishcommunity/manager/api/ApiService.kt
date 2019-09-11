package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.Dynamic
import com.example.rubbishcommunity.model.Vote
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.login.LoginRequestModel
import com.example.rubbishcommunity.model.api.login.LoginResultModel
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {
	
	@POST("api/account/login")
	fun login(@Body loginRequestModel: LoginRequestModel): Single<ResultModel<LoginResultModel>>
	
	
	@Headers("accept: application/json;charset=UTF-8")
	@GET("api/account/info/{userId}/get")
	fun getUserInfo(@Path("userId")userId:String): Single<String>
	
	
	
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