package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.message.FetchChatMessageListRequestModel
import com.example.rubbishcommunity.model.api.message.SendChatMessageRequestModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/11/20 14：12
 */
interface ChatService {
	
	//获取对消息
	@GET("api/message/fetch")
	fun fetchChatMessageList(@Body fetchChatMessageListRequestModel: FetchChatMessageListRequestModel): Single<ResultModel<ResponseBody>>
	
	//发送对话消息
	@POST("api/message/persist")
	fun sendMsgToPeople(@Body sendChatMessageRequestModel: SendChatMessageRequestModel): Single<ResultModel<ResponseBody>>
	
}