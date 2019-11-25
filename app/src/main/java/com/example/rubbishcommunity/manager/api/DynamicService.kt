package com.example.rubbishcommunity.manager.api


import com.example.rubbishcommunity.model.Dynamic
import com.example.rubbishcommunity.model.Vote
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicRequestModel
import com.example.rubbishcommunity.model.api.release.ReleaseDynamicResultModel
import com.example.rubbishcommunity.model.api.release.draft.ClearDraftResultModel
import com.example.rubbishcommunity.model.api.release.draft.Draft
import com.example.rubbishcommunity.model.api.release.draft.GetDraftResultModel
import io.reactivex.Single
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/11/10 14：12
 */
interface DynamicService {
	
	//动态草稿部分------------------------------------------------------------------------------------
	
	//上传动态草稿
	@Headers("Content-Type: application/json;charset=UTF-8")
	@POST("api/moments/draft")
	fun saveDraft(@Body draft: Draft): Single<ResultModel<String>>
	
	//拉取动态草稿
	@Headers("Content-Type: application/json;charset=UTF-8")
	@GET("api/moments/draft")
	fun getDraft(): Single<ResultModel<GetDraftResultModel>>
	
	//清空动态草稿
	@DELETE("api/moments/draft")
	fun clearDraft(): Single<ResultModel<ClearDraftResultModel>>
	
	//----------------------------------------------------------------------------------------------
	
	
	
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
	
	
}