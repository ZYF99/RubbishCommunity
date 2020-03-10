package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.Vote
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.moments.GetMomentsByClassifyRequestModel
import com.example.rubbishcommunity.model.api.moments.GetMomentsRequestModel
import com.example.rubbishcommunity.model.api.moments.GetMomentsResultModel
import com.example.rubbishcommunity.model.api.moments.MomentCommentRequestModel
import com.example.rubbishcommunity.model.api.release.ReleaseMomentRequestModel
import com.example.rubbishcommunity.model.api.release.ReleaseMomentResultModel
import com.example.rubbishcommunity.model.api.release.draft.ClearDraftResultModel
import com.example.rubbishcommunity.model.api.release.draft.Draft
import com.example.rubbishcommunity.model.api.release.draft.GetDraftResultModel
import com.example.rubbishcommunity.model.api.release.draft.SaveDraftResultModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/11/10 14：12
 */
interface MomentService {
	
	//动态草稿部分------------------------------------------------------------------------------------
	
	//上传动态草稿
	@Headers("Content-Type: application/json;charset=UTF-8")
	@POST("api/moments/draft")
	fun saveDraft(@Body draft: Draft): Single<ResultModel<SaveDraftResultModel>>
	
	//拉取动态草稿
	@Headers("Content-Type: application/json;charset=UTF-8")
	@GET("api/moments/draft")
	fun getDraft(): Single<ResultModel<GetDraftResultModel?>>
	
	//清空动态草稿
	@DELETE("api/moments/draft")
	fun clearDraft(): Single<ResultModel<ClearDraftResultModel>>
	
	//----------------------------------------------------------------------------------------------
	
	
	//发布动态
	@POST("api/moments/publish")
	fun releaseMoment(@Body releaseMomentRequestModel: ReleaseMomentRequestModel): Single<ResultModel<ReleaseMomentResultModel>>
	
	//按地理位置拉取moments列表
	@POST("api/moments/location/fetch")
	fun fetchMomentsByLocation(@Body getMomentsByLocationRequestModel: GetMomentsRequestModel): Single<ResultModel<GetMomentsResultModel>>
	
	//按classify拉取moments列表
	@POST("api/moments/classify/fetch")
	fun fetchMomentsByClassify(@Body getMomentsByClassifyRequestModel: GetMomentsByClassifyRequestModel): Single<ResultModel<GetMomentsResultModel>>
	
	//评论moments
	@POST("api/moments/comment")
	fun pushCommentOrLike(@Body momentCommentRequestModel: MomentCommentRequestModel):Single<ResponseBody>
	
	//点赞moments
	
	@GET("votes?")
	fun getVoteList(@Query("offset") offset: Int): Single<MutableList<Vote>>
	
	
}