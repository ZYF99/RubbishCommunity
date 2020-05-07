package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.message.AddFriendsRequestModel
import com.example.rubbishcommunity.model.api.message.FetchAllLikeMeRequestListRequestModel
import com.example.rubbishcommunity.model.api.message.FetchAllLikeMeRequestListResultModel
import com.example.rubbishcommunity.model.api.message.UserMatchRelationRespListModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RelationService {
	
	//拉取好友列表
	@GET("api/relation/persist/match")
	fun fetchFriendsList(): Single<ResultModel<UserMatchRelationRespListModel>>
	
	//添加好友
	@POST("api/relation/persist")
	fun addFriends(@Body addFriendsRequestModel: AddFriendsRequestModel): Single<ResponseBody>
	
	//查询所有好友请求
	@POST("api/relation/persist/search")
	fun fetchAllLikeRequestList(@Body fetchAllLikeMeRequestListRequestModel: FetchAllLikeMeRequestListRequestModel): Single<ResultModel<FetchAllLikeMeRequestListResultModel>>
}