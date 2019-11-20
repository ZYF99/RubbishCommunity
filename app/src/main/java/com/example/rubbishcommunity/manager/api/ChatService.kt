package com.example.rubbishcommunity.manager.api


import com.example.rubbishcommunity.model.Message
import io.reactivex.Single
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/11/20 14：12
 */
interface ChatService {
	
	//获取消息列表
	@GET("msgList")
	fun getMessageList(@Query("offset") offset: Int): Single<MutableList<Message>>
	
	//发送消息
	@POST("chat")
	fun sendMsgToPeople(@Query("msg") msg: String): Single<String>
	
}